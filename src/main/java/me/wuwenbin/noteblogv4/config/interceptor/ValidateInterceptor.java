package me.wuwenbin.noteblogv4.config.interceptor;

import me.wuwenbin.noteblogv4.config.session.NBContext;
import me.wuwenbin.noteblogv4.config.session.NBSession;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.util.CookieUtils;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 每个访问路径都需要做的一些操作
 * 譬如user的信息放入session
 * created by Wuwenbin on 2018/1/23 at 13:41
 */
public class ValidateInterceptor extends HandlerInterceptorAdapter {
    private NBContext blogContext;

    public ValidateInterceptor(NBContext blogContext) {
        this.blogContext = blogContext;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Cookie cookie = CookieUtils.getCookie(request, NoteBlogV4.Session.SESSION_ID_COOKIE);
        if (cookie != null) {
            String sessionId = cookie.getValue();
            NBSession blogSession = blogContext.get(sessionId);
            if (blogSession != null) {
                blogSession.update();
                if (modelAndView != null) {
                    modelAndView.getModelMap().addAttribute("su", NBUtils.user2Map(blogSession.getSessionUser()));
                }
            }
        }

    }
}
