package me.wuwenbin.noteblogv4.config.interceptor;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.util.CookieUtils;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 虽然{@code @NBAuth}也能够处理权限拦截
 * 此处的拦截器更多的是为了处理Session和Cookie的一些逻辑
 * created by Wuwenbin on 2018/1/23 at 13:41
 *
 * @author wuwenbin
 */
public class AdminInterceptor extends HandlerInterceptorAdapter {

    private NBContext blogContext;

    public AdminInterceptor(NBContext blogContext) {
        this.blogContext = blogContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = CookieUtils.getCookie(request, NoteBlogV4.Session.SESSION_ID_COOKIE);
        if (cookie != null) {
            String sessionId = cookie.getValue();
            NBSysUser sessionUser = blogContext.getSessionUser(sessionId);
            if (sessionUser == null) {
                handleAjaxRequest(request, response);
                return false;
            } else if (sessionUser.getDefaultRoleId() == blogContext.getApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID)) {
                return true;
            } else {
                if (NBUtils.isAjaxRequest(request)) {
                    JSONObject jsonObject = JSONUtil.createObj();
                    jsonObject.putAll(NBR.error("非法访问，即将跳转首页！", NoteBlogV4.Session.FRONTEND_INDEX));
                    response.getWriter().write(jsonObject.toString());
                } else {
                    response.sendRedirect(NoteBlogV4.Session.FRONTEND_INDEX);
                }
                return false;
            }
        }
        return false;
    }

    static void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (NBUtils.isRouterRequest(request)) {
            response.setCharacterEncoding("UTF-8");
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.putAll(NBR.custom(-1, "用户未登录或登录时效过期，请重新登录！", NoteBlogV4.Session.LOGIN_URL));
            response.getWriter().write(jsonObject.toString());
        } else if (NBUtils.isAjaxRequest(request) && !NBUtils.isRouterRequest(request)) {
            response.setCharacterEncoding("UTF-8");
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.putAll(NBR.error("用户未登录或登录时效过期，请重新登录！", NoteBlogV4.Session.LOGIN_URL));
            response.getWriter().write(jsonObject.toString());
        } else {
            response.sendRedirect(NoteBlogV4.Session.LOGIN_URL);
        }
    }
}
