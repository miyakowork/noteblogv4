package me.wuwenbin.noteblogv4.web.frontend.content;


import cn.hutool.core.util.StrUtil;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CommentRepository;
import me.wuwenbin.noteblogv4.dao.repository.KeywordRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.entity.NBComment;
import me.wuwenbin.noteblogv4.model.entity.NBKeyword;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * created by Wuwenbin on 2018/2/8 at 18:54
 */
@Controller
@RequestMapping("/token/comment")
public class CommentController extends BaseController {

    private final CommentRepository commentRepository;
    private final KeywordRepository keywordRepository;
    private final ParamRepository paramRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, KeywordRepository keywordRepository, ParamRepository paramRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.keywordRepository = keywordRepository;
        this.paramRepository = paramRepository;
        this.articleRepository = articleRepository;
    }

    @PostMapping("/sub")
    @ResponseBody
    public NBR sub(@Valid NBComment comment, BindingResult bindingResult, HttpServletRequest request) {
        if (!paramRepository.findValueByName("all_comment_open").equals("1") || !articleRepository.findOne(comment.getArticleId()).getCommented()) {
            return error("未开放评论功能，请勿非法操作！");
        }
        if (!bindingResult.hasErrors()) {
            comment.setIpAddr(WebUtils.getRemoteAddr(request));
            comment.setIpCnAddr(BlogUtils.getIpCnInfo(BlogUtils.getIpInfo(comment.getIpAddr())));
            comment.setUserAgent(request.getHeader("user-agent"));
            comment.setComment(Injection.stripSqlXSS(comment.getComment()));
            List<NBKeyword> keywords = keywordRepository.findAll();
            keywords.forEach(kw -> comment.setComment(comment.getComment().replace(kw.getWords(), StrUtil.repeat("*", kw.getWords().length()))));
            return builder("发表评论成功", "发表评论失败", "发表评论失败").exec(() -> commentRepository.save(comment) != null);
        } else {
            return error("提交的评论内容不合法");
        }
    }
}
