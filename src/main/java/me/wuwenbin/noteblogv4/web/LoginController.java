package me.wuwenbin.noteblogv4.web;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static me.wuwenbin.noteblogv4.model.pojo.framework.NBR.error;
import static me.wuwenbin.noteblogv4.model.pojo.framework.NBR.ok;

/**
 * created by Wuwenbin on 2018/7/19 at 20:54
 */
@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final NBContext blogContext;

    @Autowired
    public LoginController(UserRepository userRepository, NBContext blogContext) {
        this.userRepository = userRepository;
        this.blogContext = blogContext;
    }

    /**
     * 登录页面
     *
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@CookieValue(value = NoteBlogV4.Session.SESSION_ID_COOKIE, required = false) String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return "login";
        }
        NBSysUser u = blogContext.getSessionUser(uuid);
        if (u != null && u.getDefaultRoleId() == NoteBlogV4.Session.WEBMASTER_ID) {
            return "management/index";
        }
        return "login";
    }

    /**
     * 登录执行方法
     *
     * @param request
     * @param response
     * @param bmyName
     * @param bmyPass
     * @param remember
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public NBR login(HttpServletRequest request, HttpServletResponse response,
                     String bmyName, String bmyPass, Boolean remember) {

        String md52Pass = SecureUtil.md5(bmyPass);

        NBSysUser findUser = userRepository
                .findByUsernameAndPasswordAndEnableTrue(bmyName, md52Pass);

        if (findUser != null) {
            if (remember != null && remember.equals(Boolean.TRUE)) {
                String cookieValue = Base64.encode(findUser.getUsername())
                        .concat(NoteBlogV4.Session.COOKIE_SPLIT).concat(findUser.getPassword());

                CookieUtils.setCookie(response, NoteBlogV4.Session.REMEMBER_COOKIE_NAME,
                        cookieValue, 15 * 24 * 60 * 60);
            }

            blogContext.setSessionUser(request, response, findUser);
            String redirectUrl =
                    findUser.getId() == NoteBlogV4.Session.WEBMASTER_ID
                            ? NoteBlogV4.Session.MANAGEMENT_INDEX
                            : NoteBlogV4.Session.FRONTEND_INDEX;
            return ok("登陆成功！", redirectUrl);
        } else {
            return error("用户名或密码错误！");
        }
    }
}
