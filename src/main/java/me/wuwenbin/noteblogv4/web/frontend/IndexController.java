package me.wuwenbin.noteblogv4.web.frontend;

import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.ParamValue.PAGE_MODERN_BUTTON;
import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.ParamValue.PAGE_MODERN_DEFAULT;

/**
 * created by Wuwenbin on 2018/7/31 at 21:33
 *
 * @author wuwenbin
 */
@Controller
public class IndexController extends BaseController {

    private final ParamRepository paramRepository;
    private final ArticleRepository articleRepository;
    private final CateRepository cateRepository;

    @Autowired
    public IndexController(ParamRepository paramRepository, ArticleRepository articleRepository, CateRepository cateRepository) {
        this.paramRepository = paramRepository;
        this.articleRepository = articleRepository;
        this.cateRepository = cateRepository;
    }

    @RequestMapping(value = {"", "/index"})
    public String index(Model model) {
        String pageModern = paramRepository.findByName(NoteBlogV4.Param.PAGE_MODERN).getValue();
        model.addAttribute("articleCount", articleRepository.count());
        model.addAttribute("cateList", cateRepository.findAll());
        model.addAttribute("articles");
        return handleStyle(
                "frontend/index/index_simple",
                () -> {
                    if (PAGE_MODERN_DEFAULT.equalsIgnoreCase(pageModern)) {
                        return "frontend/index/index_flow";
                    } else if (PAGE_MODERN_BUTTON.equalsIgnoreCase(pageModern)) {
                        return "frontend/index/index_pagination";
                    } else {
                        return "redirect:/error?errorCode=404";
                    }
                }, paramRepository
        );
    }
}
