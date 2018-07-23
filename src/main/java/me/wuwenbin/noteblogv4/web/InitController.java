package me.wuwenbin.noteblogv4.web;

import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import me.wuwenbin.noteblogv4.util.FontAwesomeUtil;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * created by Wuwenbin on 2018/7/16 at 14:10
 *
 * @author wuwenbin
 */
@Controller
public class InitController {

    private final ParamService paramService;

    @Autowired
    public InitController(ParamService paramService) {
        this.paramService = paramService;
    }

    @RequestMapping("/init")
    public String init() {
        boolean initialization =
                paramService.getValueByName(NoteBlogV4.Param.INIT_STATUS)
                        .equals(NoteBlogV4.Init.INIT_NOT);
        return initialization ? "init" : "redirect:/";
    }

    @NBAuth("b")
    @RequestMapping("/b")
    public String b(HttpServletRequest request) {
        String fontawesome = NBUtils.getFilePathInClassesPath("static/plugins/font-awesome/css/font-awesome.css");
        List<String> a = FontAwesomeUtil.getAllFonts(fontawesome);
        request.setAttribute("fonts", a);
        return "b";
    }

    @RequestMapping("initUser")
    @ResponseBody
    public NBSysUser insertUser(HttpServletRequest request, HttpServletResponse response) {
        NBContext nbContext = NBUtils.getBean(NBContext.class);
        NBSysUser u
                = NBSysUser.builder()
                .username("admin")
                .password(SecureUtil.md5(SecureUtil.md5("123456")))
                .defaultRoleId(nbContext.getApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID))
                .nickname("管理员")
                .build();
        UserRepository userRepository = NBUtils.getBean(UserRepository.class);
        NBSysUser u2 = userRepository.save(u);
//        nbContext.setSessionUser(request, response, u2);
        return u2;
    }

}
