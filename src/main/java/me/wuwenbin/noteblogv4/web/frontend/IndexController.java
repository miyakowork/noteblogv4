package me.wuwenbin.noteblogv4.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/7/31 at 21:33
 * @author wuwenbin
 */
@Controller
public class IndexController {

    @RequestMapping(value = {"", "/index"})
    public String index(Model model) {
        return "";
    }
}
