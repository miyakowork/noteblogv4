package me.wuwenbin.noteblogv4.config.interceptor;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.date.DateUnit;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.application.NBSession;
import me.wuwenbin.noteblogv4.dao.repository.LoggerRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBLogger;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.business.IpInfo;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import me.wuwenbin.noteblogv4.util.CookieUtils;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.core.env.Environment;
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
    private ParamService paramService;

    public ApplicationInterceptor(NBContext blogContext, ParamService paramService) {
        this.blogContext = blogContext;
        this.paramService = paramService;
    }

    /**
     * 判断是否走初始化页面
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String init = "/init", initSubmit = "/init/submit";
        boolean initialize = paramService.getValueByName(NoteBlogV4.Init.INIT_STATUS).equals(NoteBlogV4.Init.INIT_SURE);
        boolean initPage = init.equals(request.getRequestURI()) || initSubmit.equals(request.getRequestURI());
        if (initialize) {
            if (!initPage) {
                return true;
            } else {
                response.sendRedirect(NoteBlogV4.Session.FRONTEND_INDEX);
                return false;
            }
        } else {
            if (initPage) {
                return true;
            } else {
                response.sendRedirect(NoteBlogV4.Session.INIT_PAGE);
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        WeakCache<String, IpInfo> ipInfoCache = blogContext.getApplicationObj("ipCacheBean");
        if (ipInfoCache == null) {
            ipInfoCache = CacheUtil.newWeakCache(DateUnit.MINUTE.getMillis() * 10);
            blogContext.setApplicationObj("ipCacheBean", ipInfoCache);
        }
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

        final String key = "noteblog.develop";
        boolean develop = NBUtils.getBean(Environment.class).getProperty(key, Boolean.class, true);

        String ipAddr = NBUtils.getRemoteAddress(request);
        IpInfo cacheInfo;
        if (!develop) {
            cacheInfo = ipInfoCache.get(ipAddr + "ipCache");
            if (cacheInfo == null) {
                ipInfoCache.put(ipAddr + "ipCache", NBUtils.getIpInfo(ipAddr));
            }
        }
        boolean openAnalysis = paramService.isOpenStatisticAnalysis();
        if (openAnalysis) {
            cacheInfo = ipInfoCache.get(ipAddr + "ipCache");
            NBLogger logger = NBLogger.builder()
                    .ipAddr(ipAddr)
                    .ipInfo(develop ? "开发中内网地址" : NBUtils.getIpCnInfo(cacheInfo))
                    .sessionId(sessionId)
                    .time(LocalDateTime.now())
                    .url(request.getRequestURL().toString())
                    .userAgent(request.getHeader("User-Agent"))
                    .username(username)
                    .requestMethod(request.getMethod())
                    .contentType(request.getContentType())
                    .build();
            NBUtils.getBean(LoggerRepository.class).saveAndFlush(logger);
        }
    }
}
