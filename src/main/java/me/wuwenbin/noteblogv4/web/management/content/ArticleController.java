package me.wuwenbin.noteblogv4.web.management.content;

import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.model.pojo.vo.NBArticleVO;
import me.wuwenbin.noteblogv4.service.content.ArticleService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(CateRepository cateRepository, NBContext context, ArticleService articleService, ArticleRepository articleRepository) {
        this.cateRepository = cateRepository;
        this.context = context;
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @RequestMapping("/article/post")
    @NBAuth(value = "management:article:post_page", remark = "博文发布页面", type = NAV_LINK, group = ROUTER)
    public String article(Model model) {
        model.addAttribute("cateList", cateRepository.findAll());
        return "management/content/article_post";
    }

    @RequestMapping(value = "/article", method = RequestMethod.GET)
    @NBAuth(value = "management:article:list_page", remark = "博文管理页面", type = NAV_LINK, group = ROUTER)
    public String articleList() {
        return "management/content/article_list";
    }

    @RequestMapping(value = "/article/list", method = RequestMethod.GET)
    @NBAuth(value = "management:article:list_data", remark = "博文管理页面中的数据接口", group = AJAX)
    @ResponseBody
    public LayuiTable<NBArticleVO> articleList(Pagination<NBArticleVO> pagination, String title) {
        Page<NBArticleVO> page = articleService.findPageInfo(pagination, title);
        return layuiTable(page);
    }

    @RequestMapping("/article/create")
    @NBAuth(value = "management:article:create", remark = "发布一篇新的博文", group = AJAX)
    @ResponseBody
    public NBR articleCreate(@Valid NBArticle article, BindingResult result, String tagNames, @CookieValue(NoteBlogV4.Session.SESSION_ID_COOKIE) String uuid) {
        if (result.getErrorCount() == 0) {
            article.setAuthorId(context.getSessionUser(uuid).getId());
            articleService.createArticle(article, tagNames);
            return NBR.ok("发表文章成功！");
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @RequestMapping("/article/edit/appreciable/{id}")
    @ResponseBody
    @NBAuth(value = "management:article:edit_appreciable", remark = "修改文章的可赞赏状态", group = AJAX)
    public NBR appreciable(@PathVariable("id") Long id, Boolean appreciable) {
        return ajaxDone(
                () -> articleRepository.updateAppreciableById(appreciable, id) == 1
                , () -> "修改打赏状态"
        );
    }

    @RequestMapping("/article/edit/commented/{id}")
    @ResponseBody
    @NBAuth(value = "management:article:edit_commented", remark = "修改文章的可评论状态", group = AJAX)
    public NBR commented(@PathVariable("id") Long id, Boolean commented) {
        return ajaxDone(
                () -> articleRepository.updateCommentedById(commented, id) == 1
                , () -> "修改评论状态"
        );
    }

    @RequestMapping("/article/edit/top/{id}")
    @ResponseBody
    @NBAuth(value = "management:article:edit_top", remark = "修改文章的置顶状态", group = AJAX)
    public NBR top(@PathVariable("id") Long id, Boolean top) {
        return ajaxDone(
                () -> articleService.updateTopById(id, top)
                , () -> "修改置顶状态"
        );
    }

    @RequestMapping("/article/delete/{id}")
    @ResponseBody
    @NBAuth(value = "management:article:delete", remark = "删除文章操作", group = AJAX)
    public NBR delete(@PathVariable("id") Long id) {
        return ajaxDone(id
                , articleRepository::deleteById
                , () -> "删除文章"
        );
    }

}
