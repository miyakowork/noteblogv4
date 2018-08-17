package me.wuwenbin.noteblogv4.web;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * created by Wuwenbin on 2018/7/17 at 17:09
 *
 * @author wuwenbin
 */
@Slf4j
public abstract class BaseController {

    /**
     * 基路径
     *
     * @param request
     * @return
     */
    protected static String basePath(HttpServletRequest request) {
        String bathPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        log.info("当前域名：[{}]", bathPath);
        return bathPath;
    }

    /**
     * 与 mybatis 的Page互转
     *
     * @param page
     * @param <T>
     * @return
     */
    protected static <T> LayuiTable<T> layuiTable(Page<T> page) {
        Pagination<T> pagination = new Pagination<>(page.getResult());
        pagination.setAutoCount(true);
        pagination.setTotalCount(page.getTotal());
        pagination.setOrderBy(page.getOrderBy());
        pagination.setPage(page.getPageNum());
        pagination.setLimit(page.getPageSize());
        return new LayuiTable<>(pagination.getTotalCount(), pagination.getTResult());
    }

    /**
     * 与 jpa 的Page互转，单列排序
     *
     * @param jpaPage
     * @param <T>
     * @return
     */
    protected static <T> LayuiTable<T> layuiTable(org.springframework.data.domain.Page<T> jpaPage, Pageable pageable) {
        Pagination<T> pagination = new Pagination<>(jpaPage.getContent());
        pagination.setAutoCount(true);
        pagination.setTotalCount(jpaPage.getTotalElements());
        //layuiTable仅支持单排序，所以此处只做首个字段
        Sort sort = jpaPage.getSort();
        int cnt = 0;
        while (cnt < 1 && sort.iterator().hasNext()) {
            Sort.Order order = sort.iterator().next();
            pagination.setSort(order.getProperty());
            pagination.setOrder(order.getDirection().name());
            cnt++;
        }
        pagination.setPage(jpaPage.getTotalPages());
        pagination.setLimit(pageable.getPageSize());
        return new LayuiTable<>(pagination.getTotalCount(), pagination.getTResult());
    }

    /**
     * 获取 jpa 的排序对象 sort
     *
     * @param page
     * @param <T>
     * @return
     */
    protected <T> Sort getJpaSort(Pagination<T> page) {
        final String asc = "asc", desc = "desc";
        String orderField = page.getSort();
        String orderDirection = page.getOrder();
        if (!StringUtils.isEmpty(orderField)) {
            if (StringUtils.isEmpty(orderDirection)) {
                return Sort.by(Sort.Order.by(orderField));
            } else {
                if (asc.equalsIgnoreCase(orderDirection)) {
                    return Sort.by(Sort.Order.asc(orderField));
                } else if (desc.equalsIgnoreCase(orderDirection)) {
                    return Sort.by(Sort.Order.desc(orderField));
                }
            }
        }
        return Sort.unsorted();
    }

    protected boolean isAjax(HttpServletRequest request) {
        return NBUtils.isAjaxRequest(request);
    }

    protected boolean isJson(HttpServletRequest request) {
        return NBUtils.isJson(request);
    }

    protected boolean isRouter(HttpServletRequest request) {
        return NBUtils.isRouterRequest(request);
    }

    protected boolean isGet(HttpServletRequest request) {
        return NBUtils.isGetRequest(request);
    }

    protected String getParam(HttpServletRequest request, String name) {
        return WebUtils.findParameterValue(request, name);
    }


    /**
     * jsr303验证处理的错误信息
     *
     * @param fieldErrors
     * @return
     */
    protected NBR ajaxJsr303(List<FieldError> fieldErrors) {
        StringBuilder message = new StringBuilder();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(":").append(error.getDefaultMessage());
        }
        return NBR.error(message.toString());
    }

    /**
     * try catch且带有if条件判断的ajax处理
     *
     * @param ifCondition
     * @return
     */
    protected NBR ajaxDone(Supplier<Boolean> ifCondition, Supplier<String> operationInfo) {
        try {
            boolean res = ifCondition.get();
            if (res) {
                String msg = operationInfo.get() == null ? "操作成功" : operationInfo.get().concat("成功！");
                return NBR.ok(msg);
            } else {
                String msg = operationInfo.get() == null ? "操作失败" : operationInfo.get().concat("失败！");
                return NBR.error(msg);
            }
        } catch (Exception e) {
            log.error("{} 出现异常，异常信息：{}", operationInfo.get(), e.getMessage());
            String msg = operationInfo.get() == null ? "操作出现异常" : operationInfo.get().concat("异常，异常信息：").concat(e.getMessage());
            return NBR.error(msg);
        }
    }

    /**
     * 类似如下的判定方式使用此方法：
     * if(ifc){
     * ifo
     * }else{
     * elseMsg
     * }
     *
     * @param ifc
     * @param ifo
     * @param elseMsg
     * @return
     */
    protected NBR ajaxDone(Supplier<Boolean> ifc, Supplier<NBR> ifo, Supplier<String> elseMsg) {
        if (ifc.get()) {
            return ifo.get();
        } else {
            return NBR.error(elseMsg.get());
        }
    }

    /**
     * try catch的ajax处理
     *
     * @param id
     * @param operation
     * @param operationInfo
     * @return
     */
    protected NBR ajaxDone(Long id, Consumer<Long> operation, Supplier<String> operationInfo) {
        try {
            operation.accept(id);
            String msg = operationInfo.get() == null ? "操作成功" : operationInfo.get().concat("成功！");
            return NBR.ok(msg);
        } catch (Exception e) {
            log.error("{} 出现异常，异常信息：{}", operationInfo.get(), e.getMessage());
            String msg = operationInfo.get() == null ? "操作出现异常" : operationInfo.get().concat("异常，异常信息：").concat(e.getMessage());
            return NBR.error(msg);
        }
    }
}
