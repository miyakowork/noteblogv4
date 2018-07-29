package me.wuwenbin.noteblogv4.web;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.util.StringUtils.isEmpty;

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

    protected boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(header);
    }

    protected boolean isJson(HttpServletRequest request) {
        String headerAccept = request.getHeader("Accept");
        return !isEmpty(headerAccept) && headerAccept.contains("application/json");
    }

    protected boolean isRouter(HttpServletRequest request) {
        String headerAccept = request.getHeader("Accept");
        return !isEmpty(headerAccept) && headerAccept.contains("text/html") && !isJson(request) && isAjax(request) && isGet(request);
    }

    protected boolean isGet(HttpServletRequest request) {
        String method = request.getMethod();
        return "GET".equalsIgnoreCase(method);
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
    public NBR ajaxDone(Supplier<Boolean> ifCondition, Supplier<String> operationInfo) {
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
}
