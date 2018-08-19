package me.wuwenbin.noteblogv4.web.management.content;

import cn.hutool.http.HtmlUtil;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.model.entity.NBNote;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.service.content.NoteService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;

/**
 * created by Wuwenbin on 2018/8/18 at 10:14
 *
 * @author wuwenbin
 */
@RequestMapping("/management/note")
@Controller
public class NoteController extends BaseController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping("/post")
    @NBAuth(value = "management:note:post_page", remark = "随笔/笔记发布页面", type = NAV_LINK, group = ROUTER)
    public String notePost() {
        return "management/content/note_post";
    }

    @RequestMapping("/create")
    public NBR noteCreate(@Valid NBNote nbNote, BindingResult result, String tagNames) {
        if (result.getErrorCount() == 0) {
            nbNote.setClearContent(HtmlUtil.cleanHtmlTag(nbNote.getContent()));
            return ajaxDone(
                    note -> noteService.createNote(note, tagNames),
                    nbNote,
                    "创建随笔/笔记成功！",
                    "创建随笔/笔记失败！");
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }
}
