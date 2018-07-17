package me.wuwenbin.noteblogv4.util;

import cn.hutool.core.map.MapUtil;
import me.wuwenbin.noteblogv4.model.entity.NBUser;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * created by Wuwenbin on 2018/7/14 at 10:33
 *
 * @author wuwenbin
 */
public class NBUtils {

    /**
     * 获取实际ip地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddress(HttpServletRequest request) {
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

    /**
     * 删除不必要的信息，避免暴露过多信息
     *
     * @param user
     * @return
     */
    public static Map<Object, Object> user2Map(NBUser user) {
        if (user == null) {
            return null;
        }
        return MapUtil.of(new Object[][]{
                {"id", user.getId()},
                {"nickname", user.getNickname()},
                {"avatar", user.getAvatar()},
                {"dri", user.getDefaultRoleId()}
        });
    }
}
