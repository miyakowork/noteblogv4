package me.wuwenbin.noteblogv4.config.interceptor;

import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.application.NBSession;
import me.wuwenbin.noteblogv4.dao.repository.LoggerRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBLogger;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.business.IpInfo;
import me.wuwenbin.noteblogv4.util.CookieUtils;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 每个访问路径都需要做的一些操作
 * 譬如user的信息放入session
 * created by Wuwenbin on 2018/1/23 at 13:41
 *
 * @author wuwenbin
 */
public class ApplicationInterceptor extends HandlerInterceptorAdapter {

    private NBContext blogContext;

    public ApplicationInterceptor(NBContext blogContext) {
        this.blogContext = blogContext;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String sessionId = "", username = "";
        Cookie cookie = CookieUtils.getCookie(request, NoteBlogV4.Session.SESSION_ID_COOKIE);
        if (cookie != null) {
            sessionId = cookie.getValue();
            NBSession blogSession = blogContext.get(sessionId);
            if (blogSession != null) {
                blogSession.update();
                if (modelAndView != null) {
                    NBSysUser user = blogSession.getSessionUser();
                    username = user.getUsername();
                    modelAndView.getModelMap().addAttribute("su", NBUtils.user2Map(user));
                }
            }
        }
        String ipAddr = NBUtils.getRemoteAddress(request);
        IpInfo ipInfo = NBUtils.getIpInfo(ipAddr);
        NBLogger logger = NBLogger.builder()
                .ipAddr(ipAddr)
                .ipInfo(NBUtils.getIpCnInfo(ipInfo))
                .sessionId(sessionId)
                .time(LocalDateTime.now())
                .url(request.getRequestURL().toString())
                .userAgent(request.getHeader("User-Agent"))
                .username(username)
                .requestMethod(request.getMethod())
                .build();
        NBUtils.getBean(LoggerRepository.class).saveAndFlush(logger);
    }
}
