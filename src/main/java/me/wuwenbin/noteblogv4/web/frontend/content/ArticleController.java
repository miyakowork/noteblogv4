package me.wuwenbin.noteblogv4.web.frontend.content;

import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.exception.ArticleFetchFailedException;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.entity.NBComment;
import me.wuwenbin.noteblogv4.model.pojo.bo.CommentQueryBO;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.service.content.CommentService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * created by Wuwenbin on 2018/9/5 at 下午12:34
 *
 * @author wuwenbin
 */
@Controller("frontArticleController")
@RequestMapping({"/article", "/a"})
public class ArticleController extends BaseController {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @Autowired
    public ArticleController(ArticleRepository articleRepository,
                             TagRepository tagRepository,
                             UserRepository userRepository, CommentService commentService) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

    @RequestMapping("/{aId}")
    public String article(@PathVariable("aId") Long aId, Model model, Pagination<NBComment> pagination, CommentQueryBO commentQueryBO) {
        Optional<NBArticle> fetchArticle = articleRepository.findById(aId);
        model.addAttribute("article", fetchArticle.orElseThrow(() -> new ArticleFetchFailedException("未找到相关id的文章！")));
        model.addAttribute("tags", tagRepository.findArticleTags(aId, true));
        model.addAttribute("author", userRepository.getOne(fetchArticle.get().getAuthorId()).getNickname());
        model.addAttribute("comments", commentService.findPageInfo(getPageable(pagination), commentQueryBO));
        return "frontend/content/article";
    }
}
