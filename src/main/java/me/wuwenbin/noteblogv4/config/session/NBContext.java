package me.wuwenbin.noteblogv4.config.session;

import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBUser;
import me.wuwenbin.noteblogv4.util.CookieUtils;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局的上下文对象
 * 用于操作session和cookie
 * created by Wuwenbin on 2018/7/16 at 12:25
 *
 * @author wuwenbin
 */
@Component
public class NBContext extends ConcurrentHashMap<String, NBSession> {


    public void setSessionUser(HttpServletRequest request, HttpServletResponse response, NBUser sessionUser) {
        NBSession session = NBSession.builder()
                .sessionUser(sessionUser)
                .expired(false)
                .host(NBUtils.getRemoteAddress(request))
                .build();
        Cookie cookie = CookieUtils.getCookie(request, NoteBlogV4.Session.SESSION_ID_COOKIE);
        if (cookie != null) {
            session.setId(cookie.getValue());
        }
        CookieUtils.setCookie(response, NoteBlogV4.Session.SESSION_ID_COOKIE, session.getId(), -1);
        put(session.getId(), session);
    }

    public NBUser getSessionUser(String uuid) {
        Optional<NBSession> user = Optional.ofNullable(getOrDefault(uuid, null));
        return user.map(NBSession::getSessionUser).orElse(null);
    }

    public void removeSessionUser(String uuid) {
        remove(uuid);
    }

    public void clearAll() {
        clear();
    }
}
