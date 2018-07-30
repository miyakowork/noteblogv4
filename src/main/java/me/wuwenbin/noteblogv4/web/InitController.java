package me.wuwenbin.noteblogv4.web;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import me.wuwenbin.noteblogv4.service.permission.UserPermissionService;
import me.wuwenbin.noteblogv4.util.FontAwesomeUtil;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group;

/**
 * created by Wuwenbin on 2018/7/16 at 14:10
 *
 * @author wuwenbin
 */
@Controller
public class InitController {

    private final ParamService paramService;
    private final ParamRepository paramRepository;
    private final UserPermissionService userPermissionService;

    @Autowired
    public InitController(ParamService paramService, UserPermissionService userPermissionService, ParamRepository paramRepository) {
        this.paramService = paramService;
        this.userPermissionService = userPermissionService;
        this.paramRepository = paramRepository;
    }

    @RequestMapping("/init")
    public String init() {
        boolean initialization =
                paramService.getValueByName(NoteBlogV4.Param.INIT_STATUS)
                        .equals(NoteBlogV4.Init.INIT_NOT);
        return initialization ? "init" : "redirect:/";
    }

    @RequestMapping("/init/submit")
    @ResponseBody
    public NBR initSubmit(HttpServletRequest request, String username, String password, String email) {
        paramService.saveInitParam(request.getParameterMap());
        userPermissionService.initMasterAccount(username, password, email);
        paramRepository.updateInitParam("1", "init_status");
        return NBR.ok("初始化设置成功！");
    }

    @NBAuth(value = "user:font:page", remark = "字体图标预览", group = Group.PAGE, type = ResType.NAV_LINK)
    @RequestMapping("/font/list")
    public String b(HttpServletRequest request) {
        String fontAwesome = NBUtils.getFilePathInClassesPath("static/plugins/font-awesome/css/font-awesome.css");
        List<String> a = FontAwesomeUtil.getAllFonts(fontAwesome);
        request.setAttribute("fonts", a);
        return "fonts";
    }

}
