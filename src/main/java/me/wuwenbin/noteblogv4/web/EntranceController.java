package me.wuwenbin.noteblogv4.web;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.code.kaptcha.Constants;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.business.QqLoginModel;
import me.wuwenbin.noteblogv4.model.pojo.business.SimpleLoginData;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.service.authority.AuthorityService;
import me.wuwenbin.noteblogv4.service.login.LoginService;
import me.wuwenbin.noteblogv4.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Session.SESSION_ID_COOKIE;

/**
 * created by Wuwenbin on 2018/7/19 at 20:54
 *
 * @author wuwenbin
 */
@Controller
public class EntranceController extends BaseController {

    private final UserRepository userRepository;
    private final NBContext blogContext;
    private final LoginService<SimpleLoginData> simpleLoginService;
    private final LoginService<QqLoginModel> qqLoginService;
    private final ParamRepository paramRepository;
    private final AuthorityService authorityService;

    @Autowired
    public EntranceController(UserRepository userRepository,
                              NBContext blogContext,
                              @Qualifier("simpleLogin") LoginService<SimpleLoginData> simpleLoginService,
                              ParamRepository paramRepository, AuthorityService authorityService, LoginService<QqLoginModel> qqLoginService) {

        this.userRepository = userRepository;
        this.blogContext = blogContext;
        this.simpleLoginService = simpleLoginService;
        this.paramRepository = paramRepository;
        this.authorityService = authorityService;
        this.qqLoginService = qqLoginService;
    }

    /**
     * 注册页面
     *
     * @return
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }

    /**
     * 用户注册
     *
     * @param bmyName
     * @param bmyPass
     * @param nickname
     * @return
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public NBR register(String bmyName, String bmyPass, String nickname, String vercode, HttpServletRequest request) {
        if (StringUtils.isEmpty(vercode)) {
            return NBR.error("验证码为空！");
        } else {
            String code = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (!code.equalsIgnoreCase(vercode)) {
                return NBR.error("验证码错误！");
            }
        }
        int min = 4, max = 12;
        if (StrUtil.isEmpty(bmyName) || bmyName.length() < min || bmyName.length() > max) {
            return NBR.error("用户名长度不合法，请重新输入");
        } else if (StringUtils.isEmpty(bmyPass)) {
            return NBR.error("密码格式错误！");
        } else {
            NBSysUser u = userRepository.findByUsername(bmyName);
            if (u != null) {
                return NBR.error("用户名已存在！");
            } else {
                authorityService.userRegistration(nickname, bmyPass, bmyName);
                return NBR.ok("保存成功！", NoteBlogV4.Session.LOGIN_URL);
            }
        }
    }

    /**
     * 登录页面
     *
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, @CookieValue(value = SESSION_ID_COOKIE, required = false) String uuid) {
        request.setAttribute("qqLogin", paramRepository.findByName(NoteBlogV4.Param.QQ_LOGIN));
        if (StringUtils.isEmpty(uuid)) {
            return "login";
        }
        NBSysUser u = blogContext.getSessionUser(uuid);
        if (u != null) {
            long masterRoleId = blogContext.getApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID);
            if (u.getDefaultRoleId() == masterRoleId) {
                return "redirect:/management/index";
            } else {
                return "redirect:/";
            }
        }
        return "login";
    }

    @RequestMapping("/api/qq")
    public String qqLogin(HttpServletRequest request) {
        String callbackDomain = basePath(request).concat("api/qqCallback");
        NBParam appId = paramRepository.findByName(NoteBlogV4.Param.APP_ID);
        if (appId == null || StringUtils.isEmpty(appId.getValue())) {
            return "redirect:/error?errorCode=404";
        } else {
            return "redirect:https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + appId.getValue() + "&redirect_uri=" + callbackDomain + "&state=" + System.currentTimeMillis();
        }
    }

    @RequestMapping("/api/qqCallback")
    public String qqCallback(HttpServletRequest request, HttpServletResponse response, String code) {
        String callbackDomain = basePath(request).concat("api/qqCallback");
        NBR r = qqLoginService.doLogin(QqLoginModel.builder().callbackDomain(callbackDomain).code(code).build());
        if (r.get("code").equals(200)) {
            blogContext.setSessionUser(request, response, (NBSysUser) r.get(NoteBlogV4.Session.LOGIN_USER));
            return "redirect:" + r.get("data");
        } else {
            return "redirect:/error?errorCode=404";
        }
    }


    /**
     * 普通登录执行方法
     *
     * @param request
     * @param response
     * @param requestType
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public NBR login(HttpServletRequest request, HttpServletResponse response, String requestType, SimpleLoginData data) {

        if (StringUtils.isEmpty(data.getVercode())) {
            return NBR.error("验证码为空！");
        } else {
            String code = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (code == null) {
                return NBR.custom(-1, "请刷新页面");
            }
            if (!code.equalsIgnoreCase(data.getVercode())) {
                return NBR.error("验证码错误！");
            }
        }
        data.setBmyPass(SecureUtil.md5(data.getBmyPass()));
        data.setRequest(request);
        data.setResponse(response);

        String simpleLogin = "simple", qqLogin = "qq", wechatLogin = "wechat";
        if (StrUtil.isNotEmpty(requestType)) {
            if (simpleLogin.equals(requestType)) {
                return simpleLoginService.doLogin(data);
            } else if (qqLogin.equals(requestType)) {
                //以后可能会有此种方式登录
                return NBR.error("不支持的请求");
            } else if (wechatLogin.equals(requestType)) {
                //以后可能会有此种方式登录
                return NBR.error("不支持的请求");
            }
        }
        return NBR.error("未知登录类型！");
    }


    /**
     * 注销
     *
     * @param request
     * @param response
     * @param from
     * @param uuid
     * @return
     */
    @NBAuth(value = "user:logout:page", remark = "用户注销请求地址", group = NBAuth.Group.PAGE)
    @RequestMapping(value = "/management/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request,
                         HttpServletResponse response, String from,
                         @CookieValue(SESSION_ID_COOKIE) String uuid) {
        blogContext.removeSessionUser(uuid);
        request.getSession().invalidate();
        CookieUtils.deleteCookie(request, response, NoteBlogV4.Session.REMEMBER_COOKIE_NAME);
        if (StringUtils.isEmpty(from)) {
            return "redirect:/";
        } else {
            return "redirect:" + NoteBlogV4.Session.MANAGEMENT_INDEX;
        }
    }

    /**
     * 注销
     *
     * @param request
     * @param response
     * @param from
     * @param uuid
     * @return
     */
//    @NBAuth(value = "user:logout:page", remark = "用户注销请求地址", group = NBAuth.Group.PAGE)
    @RequestMapping(value = "/token/logout", method = RequestMethod.GET)
    public String logout2(HttpServletRequest request,
                         HttpServletResponse response, String from,
                         @CookieValue(SESSION_ID_COOKIE) String uuid) {
        blogContext.removeSessionUser(uuid);
        request.getSession().invalidate();
        CookieUtils.deleteCookie(request, response, NoteBlogV4.Session.REMEMBER_COOKIE_NAME);
        if (StringUtils.isEmpty(from)) {
            return "redirect:/";
        } else {
            return "redirect:" + NoteBlogV4.Session.FRONTEND_INDEX;
        }
    }
}
