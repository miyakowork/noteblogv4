package me.wuwenbin.noteblogv4.web.management.message;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.MessageRepository;
import me.wuwenbin.noteblogv4.model.entity.NBMessage;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;
import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;

/**
 * created by Wuwenbin on 2018/8/15 at 16:28
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management/message")
public class AdminMessageController extends BaseController {

    private final MessageRepository messageRepository;

    @Autowired
    public AdminMessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @RequestMapping
    @NBAuth(value = "management:message:page", remark = "消息管理页面", group = ROUTER, type = NAV_LINK)
    public String tagIndex() {
        return "management/message/message";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:message:list", remark = "消息管理页面分页数据接口", group = AJAX)
    public LayuiTable<NBMessage> cateList(Pagination<NBMessage> messagePagination, String clearComment) {
        Sort sort = getJpaSort(messagePagination);
        Pageable pageable = PageRequest.of(messagePagination.getPage() - 1, messagePagination.getLimit(), sort);
        if (StringUtils.isEmpty(messagePagination)) {
            Page<NBMessage> tagPage = messageRepository.findAll(pageable);
            return layuiTable(tagPage, pageable);
        } else {
            Example<NBMessage> messageExample = Example.of(
                    NBMessage.builder().clearComment(clearComment).build(),
                    ExampleMatcher.matching()
                            .withMatcher("clearComment", ExampleMatcher.GenericPropertyMatcher::contains).withIgnoreCase()
                            .withIgnorePaths("post", "enable")
            );
            Page<NBMessage> commentPage = messageRepository.findAll(messageExample, pageable);
            return layuiTable(commentPage, pageable);
        }
    }


    @RequestMapping("/update")
    @ResponseBody
    @NBAuth(value = "management:message:update", remark = "修改评论状态", group = AJAX)
    public NBR delete(@RequestParam("id") Long id, boolean enable) {
        return ajaxDone(
                () -> messageRepository.updateMessageStatus(id, enable) == 1,
                () -> "修改留言"
        );
    }
}
