package me.wuwenbin.noteblogv4.web.frontend;

import cn.hutool.core.util.ArrayUtil;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String index(Model model, String style) {
        List<NBParam> params = paramRepository.findAllByLevelGreaterThanEqual(10);
        Map<String, Object> settingsMap = params.stream().collect(Collectors.toMap(NBParam::getName, NBParam::getValue));
        model.addAttribute("settings", settingsMap);
        model.addAttribute("articleCount", articleRepository.count());
        model.addAttribute("cateList", cateRepository.findAll());
        if (StringUtils.isEmpty(style)) {
            return "frontend/index/index_flow";
        } else {
            final String[] flowStyle = new String[]{"flow", "f"};
            if (ArrayUtil.contains(flowStyle, style)) {
                return "frontend/index/index_flow";
            }
            final String[] paginationStyle = new String[]{"pagination", "page", "p"};
            if (ArrayUtil.contains(paginationStyle, style)) {
                return "frontend/index/index_pagination";
            }
            final String[] simpleStyle = new String[]{"simple", "s"};
            if (ArrayUtil.contains(simpleStyle, style)) {
                return "frontend/index/index_simple";
            }
        }
        return "redirect:/error?errorCode=404";
    }
}
