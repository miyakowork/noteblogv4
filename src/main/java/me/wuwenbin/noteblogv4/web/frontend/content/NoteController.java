package me.wuwenbin.noteblogv4.web.frontend.content;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.NoteRepository;
import me.wuwenbin.noteblogv4.model.entity.NBNote;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.service.content.NoteService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by Wuwenbin on 2018/2/9 at 14:14
 *
 * @author wuwenbin
 */
@Slf4j
@Controller("frontNoteController")
@RequestMapping("/note")
public class NoteController extends BaseController {

    private final NoteRepository noteRepository;
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteRepository noteRepository, NoteService noteService) {
        this.noteRepository = noteRepository;
        this.noteService = noteService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("noteCount", noteRepository.count());
        return "frontend/content/note";
    }

    @PostMapping("/next")
    @ResponseBody
    public NBR next(Pagination<NBNote> pagination, String t, String cc) {
        Pageable pageable = getPageable(pagination);
        Page<NBNote> notePage = noteService.findNotePage(pageable, t, cc);
        return NBR.ok("获取成功", notePage);
    }
}
