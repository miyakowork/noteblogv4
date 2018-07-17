package me.wuwenbin.noteblogv4.web;

import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
