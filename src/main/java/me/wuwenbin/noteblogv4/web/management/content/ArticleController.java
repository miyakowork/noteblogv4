package me.wuwenbin.noteblogv4.web.management.content;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;

/**
 * created by Wuwenbin on 2018/8/2 at 21:24
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class ArticleController extends BaseController {

    private final CateRepository cateRepository;

    @Autowired
    public ArticleController(CateRepository cateRepository) {
        this.cateRepository = cateRepository;
    }

    @RequestMapping("/article/post")
    @NBAuth(value = "management:article:post_page", remark = "博文发布页面", type = NAV_LINK, group = ROUTER)
    public String article(Model model) {
        model.addAttribute("cates", cateRepository.findAll());
        return "management/content/article_post";
    }
}
