package me.wuwenbin.noteblogv4.web.management.content;

import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;
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
    private final NBContext context;

    @Autowired
    public ArticleController(CateRepository cateRepository, NBContext context) {
        this.cateRepository = cateRepository;
        this.context = context;
    }

    @RequestMapping("/article/post")
    @NBAuth(value = "management:article:post_page", remark = "博文发布页面", type = NAV_LINK, group = ROUTER)
    public String article(Model model) {
        model.addAttribute("cateList", cateRepository.findAll());
        return "management/content/article_post";
    }

    @RequestMapping("/article/create")
    @NBAuth(value = "management:article:create", remark = "发布一篇新的博文", group = AJAX)
    @ResponseBody
    public NBR articleCreate(@Valid NBArticle article, String tagNames, String editor, BindingResult result, @CookieValue(NoteBlogV4.Session.SESSION_ID_COOKIE) String uuid) {
        if (result.getErrorCount() == 0) {
            article.setAuthorId(context.getSessionUser(uuid).getId());
            return NBR.ok();
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }
}
