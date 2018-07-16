package me.wuwenbin.noteblogv4.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/7/16 at 14:10
 *
 * @author wuwenbin
 */
@Controller
public class InitController {

    @RequestMapping("/init")
    public String init() {
        return "init";
    }
}
