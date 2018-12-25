package me.wuwenbin.noteblogv4.web.frontend.content;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.modules.jpa.support.Page;
import me.wuwenbin.noteblog.v4.model.frontend.bo.MessageListBo;
import me.wuwenbin.noteblog.v4.model.frontend.vo.MessageVo;
import me.wuwenbin.noteblog.v4.repository.*;
import me.wuwenbin.noteblogv4.dao.repository.*;
import me.wuwenbin.noteblogv4.model.entity.NBKeyword;
import me.wuwenbin.noteblogv4.model.entity.NBMessage;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.util.NBUtils;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static me.wuwenbin.modules.utils.http.R.error;
import static me.wuwenbin.modules.utils.web.Controllers.builder;

/**
 * created by Wuwenbin on 2018/2/8 at 18:54
 */
@Controller
@RequestMapping
public class MessageController extends BaseController {

    private final MessageRepository messageRepository;
    private final KeywordRepository keywordRepository;
    private final ParamRepository paramRepository;
    private final CateRepository cateRepository;
    private final TagReferRepository tagReferRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository, KeywordRepository keywordRepository,
                             ParamRepository paramRepository, CateRepository cateRepository, TagReferRepository tagReferRepository) {
        this.messageRepository = messageRepository;
        this.keywordRepository = keywordRepository;
        this.paramRepository = paramRepository;
        this.cateRepository = cateRepository;
        this.tagReferRepository = tagReferRepository;
    }

    @RequestMapping("/message")
    public String index(Model model) {
        model.addAttribute("cates", cateRepository.findAll());
        model.addAttribute("tags", tagReferRepository.findTagsTab());
        return "frontend/message";
    }


    @PostMapping("/token/message/sub")
    @ResponseBody
    public NBR sub(@Valid NBMessage message, BindingResult bindingResult, HttpServletRequest request) {
        if (!bindingResult.hasErrors()) {
            message.setIpAddr(NBUtils.getRemoteAddress(request));
            message.setUserAgent(request.getHeader("user-agent"));
            message.setIpCnAddr(NBUtils.getIpCnInfo(NBUtils.getIpInfo(message.getIpAddr())));
            message.setComment(NBUtils.stripSqlXSS(message.getComment()));
            List<NBKeyword> keywords = keywordRepository.findAll();
            keywords.forEach(kw -> message.setComment(message.getComment().replace(kw.getWords(), StrUtil.repeat("*", kw.getWords().length()))));
            return builder("发表留言成功", "发表留言失败", "发表留言失败").exec(() -> messageRepository.save(message) != null);
        } else {
            return error("提交的留言内容不合法");
        }
    }


    @PostMapping("/message/lists")
    @ResponseBody
    public Page<MessageVo> comments(Page<MessageVo> messageVoPage, MessageListBo messageListBo) {
        return messageRepository.findPagination(messageVoPage, MessageVo.class, messageListBo);
    }
}
