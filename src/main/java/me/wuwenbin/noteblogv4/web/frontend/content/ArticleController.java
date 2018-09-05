package me.wuwenbin.noteblogv4.web.frontend.content;

import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/9/5 at 下午12:34
 *
 * @author wuwenbin
 */
@Controller("frontArticleController")
@RequestMapping({"/article", "/a"})
public class ArticleController extends BaseController {

    private final ArticleRepository articleRepository;
    @Autowired
    private ParamRepository paramRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @RequestMapping("/{aId}")
    public String article(@PathVariable("aId") Long aId, Model model) {
        model.addAttribute("article", articleRepository.getOne(aId));
        Object style=paramRepository.findByName(NoteBlogV4.Param.INDEX_STYLE);
        if (style==null){
            throw new RuntimeException("页面风格为设定！");
        }else {

        }
        return "frontend/content/article";
    }
}
