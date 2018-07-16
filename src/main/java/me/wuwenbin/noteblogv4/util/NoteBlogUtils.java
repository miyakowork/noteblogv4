package me.wuwenbin.noteblogv4.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Wuwenbin on 2018/7/14 at 10:33
 * @author wuwenbin
 */
public class NoteBlogUtils {

    /**
     * 获取实际ip地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddress;
        try {
            remoteAddress = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(remoteAddress) || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || remoteAddress.length() == 0 || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(remoteAddress) || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getRemoteAddr();
            }
        } catch (Exception var3) {
            remoteAddress = request.getRemoteAddr();
        }
        return remoteAddress;
    }
}
