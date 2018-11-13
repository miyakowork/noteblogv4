package me.wuwenbin.noteblogv4.web.management.content;

import cn.hutool.http.HtmlUtil;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.NoteRepository;
import me.wuwenbin.noteblogv4.exception.NoteFetchFailedException;
import me.wuwenbin.noteblogv4.model.constant.TagType;
import me.wuwenbin.noteblogv4.model.entity.NBNote;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.service.content.NoteService;
import me.wuwenbin.noteblogv4.service.content.TagService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;
import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.OTHER;

/**
 * created by Wuwenbin on 2018/8/18 at 10:14
 *
 * @author wuwenbin
 */
@RequestMapping("/management/note")
@Controller
public class NoteController extends BaseController {

    private final NoteService noteService;
    private final NoteRepository noteRepository;
    private final TagService tagService;

    @Autowired
    public NoteController(NoteService noteService, NoteRepository noteRepository, TagService tagService) {
        this.noteService = noteService;
        this.noteRepository = noteRepository;
        this.tagService = tagService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @NBAuth(value = "management:note:list_page", remark = "随笔管理页面", type = NAV_LINK, group = ROUTER)
    public String noteList() {
        return "management/content/note_list";
    }

    @RequestMapping("/post")
    @NBAuth(value = "management:note:post_page", remark = "随笔/笔记发布页面", type = NAV_LINK, group = ROUTER)
    public String notePost() {
        return "management/content/note_post";
    }

    @RequestMapping("/edit")
    @NBAuth(value = "management:note:edit_page", remark = "随笔管编辑页面", type = OTHER, group = ROUTER)
    public String edit(Model model, Long id) {
        Optional<NBNote> note = noteRepository.findById(id);
        model.addAttribute("editNote", note.orElseThrow(NoteFetchFailedException::new));
        return "management/content/note_edit";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @NBAuth(value = "management:note:list_data", remark = "随笔管理页面中的数据接口", group = AJAX)
    @ResponseBody
    public LayuiTable<NBNote> noteList(Pagination<NBNote> notePagination, String title, String clearContent) {
        Sort sort = getJpaSort(notePagination);
        Pageable pageable = PageRequest.of(notePagination.getPage() - 1, notePagination.getLimit(), sort);
        Page<NBNote> jpaPage = noteService.findNotePage(pageable, title, clearContent);
        return layuiTable(jpaPage, pageable);
    }

    @RequestMapping("/create")
    @NBAuth(value = "management:note:create", remark = "发布一篇新的随笔/笔记", group = AJAX)
    @ResponseBody
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

    @RequestMapping("/edit/tags")
    @ResponseBody
    @NBAuth(value = "management:note:edit_note_tags", remark = "编辑随笔/笔记页面的tag数据包含选中的(selected)", type = OTHER, group = AJAX)
    public NBR editPageNoteTags(Long id) {
        if (StringUtils.isEmpty(id)) {
            return NBR.custom(-1);
        } else {
            return NBR.custom(0, tagService.findSelectedTagsByReferId(id, TagType.note));
        }
    }

    @RequestMapping("/update")
    @NBAuth(value = "management:note:update", remark = "修改一篇随笔/笔记", group = AJAX)
    @ResponseBody
    public NBR updateNote(@Valid NBNote nbNote, BindingResult result, String tagNames) {
        if (result.getErrorCount() == 0) {
            nbNote.setModify(LocalDateTime.now());
            nbNote.setClearContent(HtmlUtil.cleanHtmlTag(nbNote.getContent()));
            return ajaxDone(
                    note -> noteService.updateNote(note, tagNames),
                    nbNote,
                    "修改随笔/笔记成功！",
                    "修改随笔/笔记失败！");
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    @NBAuth(value = "management:note:delete", remark = "删除笔记操作", group = AJAX)
    public NBR delete(@PathVariable("id") Long id) {
        return ajaxDone(id
                , noteRepository::deleteById
                , () -> "删除笔记"
        );
    }

    @RequestMapping("/update/top/{id}")
    @ResponseBody
    @NBAuth(value = "management:note:update_top", remark = "修改笔记的置顶状态", group = AJAX)
    public NBR top(@PathVariable("id") Long id, Boolean top) {
        return ajaxDone(
                () -> noteRepository.updateNoteTopStatus(id, top) == 1
                , () -> "修改置顶状态"
        );
    }

    @RequestMapping("/update/show/{id}")
    @ResponseBody
    @NBAuth(value = "management:note:update_show", remark = "修改笔记的显隐状态", group = AJAX)
    public NBR show(@PathVariable("id") Long id, Boolean show) {
        return ajaxDone(
                () -> noteRepository.updateNoteShowStatus(id, show) == 1
                , () -> "修改显示状态"
        );
    }
}
