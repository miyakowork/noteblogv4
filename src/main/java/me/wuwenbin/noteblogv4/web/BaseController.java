package me.wuwenbin.noteblogv4.web;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Page;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * created by Wuwenbin on 2018/7/17 at 17:09
 *
 * @author wuwenbin
 */
@Slf4j
public abstract class BaseController {

    protected static String basePath(HttpServletRequest request) {
        String bathPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        log.info("当前域名：[{}]", bathPath);
        return bathPath;
    }

    protected static <T> LayuiTable<T> layuiTable(Page<T> page) {
        return new LayuiTable<>(page.getTotalCount(), page.getTResult());
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

    protected NBR ajaxError(List<FieldError> fieldErrors) {
        StringBuilder message = new StringBuilder();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(":").append(error.getDefaultMessage());
        }
        return NBR.error(message.toString());
    }

}
