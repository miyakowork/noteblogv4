package me.wuwenbin.noteblogv4.web.management.content;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/8/2 at 21:24
 */
@Controller
@RequestMapping("/management")
public class ArticleController {


    @RequestMapping("/article")
    public String article(Model model) {
        return "article";
    }
}
