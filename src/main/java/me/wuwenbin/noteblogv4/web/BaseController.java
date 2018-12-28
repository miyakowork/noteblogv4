package me.wuwenbin.noteblogv4.web;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.util.CacheUtils;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.ParamValue.STYLE_NORMAL;
import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.ParamValue.STYLE_SIMPLE;

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

    protected static String handleStyle(String simple, Supplier<String> normalOrOther, ParamRepository paramRepository) {
        String style = CacheUtils.getParamCache().get(NoteBlogV4.Param.BLOG_STYLE, String.class);
        if (StringUtils.isEmpty(style)) {
            style = paramRepository.findByName(NoteBlogV4.Param.BLOG_STYLE).getValue();
            CacheUtils.getParamCache().put(NoteBlogV4.Param.BLOG_STYLE, style);
        }
        if (StringUtils.isEmpty(style)) {
            throw new RuntimeException("页面风格未设定！");
        } else {
            if (STYLE_SIMPLE.equalsIgnoreCase(style)) {
                return simple;
            } else if (STYLE_NORMAL.equalsIgnoreCase(style)) {
                return normalOrOther.get();
            } else {
                return "redirect:/error?errorCode=404";
            }
        }
    }

    protected static String handleStyle(String simple, String normal, ParamRepository paramRepository) {
        return handleStyle(simple, () -> normal, paramRepository);
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
     * 目前layui dataTable不支持多排序
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

    /**
     * 除了前台传过来的分页信息，如果需要额外手动添加一些排序信息，则使用此方法
     *
     * @param page
     * @param otherFields
     * @param <T>
     * @return
     */
    protected <T> Sort getJpaSortWithOther(Pagination<T> page, Map<String, String> otherFields) {
        final String asc = "asc", desc = "desc";
        String orderField = page.getSort();
        String orderDirection = page.getOrder();
        List<Sort.Order> orders = new ArrayList<>();
        if (!StringUtils.isEmpty(orderField)) {
            if (StringUtils.isEmpty(orderDirection)) {
                orders.add(Sort.Order.by(orderField));
            } else {
                if (asc.equalsIgnoreCase(orderDirection)) {
                    orders.add(Sort.Order.asc(orderField));
                } else if (desc.equalsIgnoreCase(orderDirection)) {
                    orders.add(Sort.Order.desc(orderField));
                }
            }
        }
        for (String filed : otherFields.keySet()) {
            String direction = otherFields.get(filed);
            if (asc.equalsIgnoreCase(direction)) {
                Sort.Order order = Sort.Order.asc(filed);
                orders.add(order);
            } else {
                Sort.Order order = Sort.Order.desc(filed);
                orders.add(order);
            }
        }
        return orders.size() > 0 ? Sort.by(orders) : Sort.unsorted();
    }

    /**
     * 通过封装的Page获取JPA的Page
     *
     * @param page
     * @param <T>
     * @return
     */
    protected <T> Pageable getPageable(Pagination<T> page) {
        Sort sort = this.getJpaSort(page);
        //jpa分页是从0开始
        return PageRequest.of(page.getPage() - 1, page.getLimit(), sort);
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
            message.append(error.getField()).append(":").append(error.getDefaultMessage()).append("<br/>");
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
     * 没有判断的返回请求
     *
     * @param operation
     * @param param
     * @param ok
     * @param err
     * @param <T>
     * @return
     */
    protected <T> NBR ajaxDone(Consumer<T> operation, T param, String ok, String err) {
        try {
            operation.accept(param);
            return NBR.ok(ok);
        } catch (Exception e) {
            log.error("请求出现异常，异常信息：{}", e.getMessage());
            return NBR.error(err);
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
