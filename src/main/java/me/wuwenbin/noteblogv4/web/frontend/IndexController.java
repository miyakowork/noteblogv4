package me.wuwenbin.noteblogv4.web.frontend;

import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.ParamValue.*;

/**
 * created by Wuwenbin on 2018/7/31 at 21:33
 *
 * @author wuwenbin
 */
@Controller
public class IndexController {

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
        List<NBParam> params = paramRepository.findAllByLevelGreaterThanEqual(10);
        Map<String, Object> settingsMap = params.stream().collect(Collectors.toMap(NBParam::getName, NBParam::getValue));
        String style = settingsMap.get(NoteBlogV4.Param.INDEX_STYLE).toString();
        String pageModern = settingsMap.get(NoteBlogV4.Param.PAGE_MODERN).toString();
        model.addAttribute("settings", settingsMap);
        model.addAttribute("articleCount", articleRepository.count());
        model.addAttribute("cateList", cateRepository.findAll());
        model.addAttribute("articles");
        if (StringUtils.isEmpty(style)) {
            throw new RuntimeException("首页风格未设定！");
        } else {
            if (INDEX_STYLE_SIMPLE.equalsIgnoreCase(style)) {
                return "frontend/index/index_simple";
            } else if (PAGE_MODERN_DEFAULT.equalsIgnoreCase(pageModern)) {
                return "frontend/index/index_flow";
            } else if (PAGE_MODERN_BUTTON.equalsIgnoreCase(pageModern)) {
                return "frontend/index/index_pagination";
            } else {
                return "redirect:/error?errorCode=404";
            }
        }
    }
}
