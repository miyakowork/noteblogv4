package me.wuwenbin.noteblogv4.web.frontend.content;

import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.exception.ArticleFetchFailedException;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * created by Wuwenbin on 2018/9/5 at 下午12:34
 *
 * @author wuwenbin
 */
@Controller("frontArticleController")
@RequestMapping({"/article", "/a"})
public class ArticleController extends BaseController {

    private final ArticleRepository articleRepository;
    private final ParamRepository paramRepository;
    private final TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, ParamRepository paramRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.paramRepository = paramRepository;
        this.tagRepository = tagRepository;
    }

    @RequestMapping("/{aId}")
    public String article(@PathVariable("aId") Long aId, Model model) {
        Optional<NBArticle> fetchArticle = articleRepository.findById(aId);
        model.addAttribute("article", fetchArticle.orElseThrow(() -> new ArticleFetchFailedException("未找到相关id的文章！")));
        List<NBParam> params = paramRepository.findAllByLevelGreaterThanEqual(10);
        Map<String, Object> settingsMap = params.stream().collect(Collectors.toMap(NBParam::getName, NBParam::getValue));
        model.addAttribute("settings", settingsMap);
        model.addAttribute("tags", tagRepository.findArticleTags(aId, true));
        model.addAttribute("author", userRepository.getOne(fetchArticle.get().getAuthorId()).getNickname());

        return handleStyle(
                "frontend/content/article_simple",
                "frontend/content/article_normal",
                paramRepository
        );
    }
}
