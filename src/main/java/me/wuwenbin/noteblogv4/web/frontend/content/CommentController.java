package me.wuwenbin.noteblogv4.web.frontend.content;


import cn.hutool.core.util.StrUtil;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CommentRepository;
import me.wuwenbin.noteblogv4.dao.repository.KeywordRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.entity.NBComment;
import me.wuwenbin.noteblogv4.model.entity.NBKeyword;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.util.NBUtils;
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

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Param.ALL_COMMENT_OPEN;

/**
 * created by Wuwenbin on 2018/2/8 at 18:54
 *
 * @author wuwenbin
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
        final String initSure = "1";
        return ajaxDone(
                () -> initSure.equals(paramRepository.findByName(ALL_COMMENT_OPEN).getValue()) && articleRepository.getOne(comment.getArticleId()).getCommented(),
                () -> {
                    if (!bindingResult.hasErrors()) {
                        comment.setIpAddr(NBUtils.getRemoteAddress(request));
                        comment.setIpCnAddr(NBUtils.getIpCnInfo(NBUtils.getIpInfo(comment.getIpAddr())));
                        comment.setUserAgent(request.getHeader("user-agent"));
                        comment.setComment(NBUtils.stripSqlXSS(comment.getComment()));
                        List<NBKeyword> keywords = keywordRepository.findAll();
                        keywords.forEach(kw -> comment.setComment(comment.getComment().replace(kw.getWords(), StrUtil.repeat("*", kw.getWords().length()))));
                        return ajaxDone(commentRepository::save, comment, "发表评论成功", "发表评论失败");
                    } else {
                        return ajaxJsr303(bindingResult.getFieldErrors());
                    }
                },
                () -> "未开放评论功能，请勿非法操作！"
        );
    }
}
