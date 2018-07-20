package me.wuwenbin.noteblogv4.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * created by Wuwenbin on 2018/7/16 at 12:38
 * @author wuwenbin
 */
public class CookieUtils {

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCurrentRequestCookieMap(request);
        return cookieMap.getOrDefault(name, null);
    }

    public static HttpServletResponse setCookie(HttpServletResponse response, String name, String value, int time) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(time);
        response.addCookie(cookie);
        return response;
    }

    public static HttpServletResponse setCookie(HttpServletResponse response, String name, String domain, String value, int time) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(time);
        response.addCookie(cookie);
        return response;
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Map<String, Cookie> cookieMap = readCurrentRequestCookieMap(request);
        loopDelCookieMap(response, name, cookieMap);
    }

    private static void loopDelCookieMap(HttpServletResponse response, String name, Map<String, Cookie> cookieMap) {
        cookieMap.forEach((k, v) -> {
            if (k.equals(name)) {
                Cookie cookie = cookieMap.get(k);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        });
    }

    private static Map<String, Cookie> readCurrentRequestCookieMap(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Map<String, Cookie> cookieMap = new HashMap<>(20);
        if (null != cookies) {
            Arrays.stream(cookies).forEach(c -> cookieMap.put(c.getName(), c));
        }
        return cookieMap;
    }
}
