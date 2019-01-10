package me.wuwenbin.noteblogv4.web.management.content;

import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.exception.ArticleFetchFailedException;
import me.wuwenbin.noteblogv4.model.constant.TagType;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.service.content.ArticleService;
import me.wuwenbin.noteblogv4.service.content.TagService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;
import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Session.SESSION_ID_COOKIE;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.OTHER;

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
    private final TagService tagService;

    @Autowired
    public ArticleController(CateRepository cateRepository, NBContext context, ArticleService articleService, ArticleRepository articleRepository, TagService tagService) {
        this.cateRepository = cateRepository;
        this.context = context;
        this.articleService = articleService;
        this.articleRepository = articleRepository;
        this.tagService = tagService;
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

    @RequestMapping("/article/edit")
    @NBAuth(value = "management:article:edit_page", remark = "博文管编辑页面", type = OTHER, group = ROUTER)
    public String edit(Model model, Long id) {
        model.addAttribute("cateList", cateRepository.findAll());
        Optional<NBArticle> article = articleRepository.findById(id);
        model.addAttribute("editArticle", article.orElseThrow(ArticleFetchFailedException::new));
        return "management/content/article_edit";
    }

    @RequestMapping("/article/edit/tags")
    @ResponseBody
    @NBAuth(value = "management:article:edit_article_tags", remark = "编辑文章页面的tag数据包含选中的(selected)", type = OTHER, group = AJAX)
    public NBR editPageArticleTags(Long id) {
        if (StringUtils.isEmpty(id)) {
            return NBR.custom(-1);
        } else {
            return NBR.custom(0, tagService.findSelectedTagsByReferId(id, TagType.article));
        }
    }

    @RequestMapping(value = "/article/list", method = RequestMethod.GET)
    @NBAuth(value = "management:article:list_data", remark = "博文管理页面中的数据接口", group = AJAX)
    @ResponseBody
    public LayuiTable<NBArticle> articleList(Pagination<NBArticle> pagination, String title, @CookieValue(SESSION_ID_COOKIE) String uuid) {
        NBSysUser user = context.getSessionUser(uuid);
        Pageable pageable = getPageable(pagination);
        Page<NBArticle> page = articleService.findPageInfo(pageable, title, user.getId());
        return layuiTable(page, pageable);
    }

    @RequestMapping("/article/create")
    @NBAuth(value = "management:article:create", remark = "发布一篇新的博文", group = AJAX)
    @ResponseBody
    public NBR articleCreate(@Valid NBArticle article, BindingResult result, String tagNames, @CookieValue(SESSION_ID_COOKIE) String uuid) {
        if (result.getErrorCount() == 0) {
            NBSysUser user = context.getSessionUser(uuid);
            article.setAuthorId(user.getId());
            articleService.createArticle(article, tagNames);
            return NBR.ok("发表文章成功！");
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }

    @RequestMapping("/article/update")
    @NBAuth(value = "management:article:update", remark = "修改一篇博文", group = AJAX)
    @ResponseBody
    public NBR articleUpdate(@Valid NBArticle article, BindingResult result, String tagNames, @CookieValue(SESSION_ID_COOKIE) String uuid) {
        if (result.getErrorCount() == 0) {
            NBSysUser user = context.getSessionUser(uuid);
            article.setAuthorId(user.getId());
            try {
                articleService.updateArticle(article, tagNames);
                return NBR.ok("修改文章成功！");
            } catch (RuntimeException e) {
                return NBR.error(e.getMessage());
            }
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @RequestMapping("/article/update/appreciable/{id}")
    @ResponseBody
    @NBAuth(value = "management:article:update_appreciable", remark = "修改文章的可赞赏状态", group = AJAX)
    public NBR appreciable(@PathVariable("id") Long id, Boolean appreciable) {
        return ajaxDone(
                () -> articleRepository.updateAppreciableById(appreciable, id) == 1
                , () -> "修改打赏状态"
        );
    }

    @RequestMapping("/article/update/commented/{id}")
    @ResponseBody
    @NBAuth(value = "management:article:update_commented", remark = "修改文章的可评论状态", group = AJAX)
    public NBR commented(@PathVariable("id") Long id, Boolean commented) {
        return ajaxDone(
                () -> articleRepository.updateCommentedById(commented, id) == 1
                , () -> "修改评论状态"
        );
    }

    @RequestMapping("/article/update/top/{id}")
    @ResponseBody
    @NBAuth(value = "management:article:update_top", remark = "修改文章的置顶状态", group = AJAX)
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
