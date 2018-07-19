package me.wuwenbin.noteblogv4.web;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import me.wuwenbin.noteblogv4.util.FontAwesomeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    @NBAuth("sss")
    @RequestMapping("/init")
    public String init() {
        boolean initialization =
                paramService.getValueByName(NoteBlogV4.Param.INIT_STATUS)
                        .equals(NoteBlogV4.Init.INIT_NOT);
        return initialization ? "init" : "redirect:/";
    }

    @RequestMapping("/b")
    public String b(HttpServletRequest request) {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fontawesome = path + "static/plugins/font-awesome/css/fontawesome.css";
        List<String> a = FontAwesomeUtil.getAllFonts(fontawesome);
        request.setAttribute("fonts", a);
        return "b";
    }

    @RequestMapping("/a")
    @ResponseBody
    public Object fonts() {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fontawesome = path + "static/plugins/font-awesome/css/fontawesome.css";
        return FontAwesomeUtil.getAllFonts(fontawesome);
    }
}
