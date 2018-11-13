package me.wuwenbin.noteblogv4.service.content;

import me.wuwenbin.noteblogv4.dao.repository.NoteRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagReferRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagRepository;
import me.wuwenbin.noteblogv4.model.constant.TagType;
import me.wuwenbin.noteblogv4.model.entity.NBNote;
import me.wuwenbin.noteblogv4.model.entity.NBTag;
import me.wuwenbin.noteblogv4.model.entity.NBTagRefer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2018/8/18 at 10:34
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final TagReferRepository tagReferRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, TagRepository tagRepository, TagReferRepository tagReferRepository) {
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
        this.tagReferRepository = tagReferRepository;
    }


    @Override
    public void createNote(NBNote nbNote, String tagNames) {
        if (StringUtils.isEmpty(tagNames)) {
            throw new RuntimeException("tagNames 不能为空！");
        }
        decorateNote(nbNote);
        NBNote n = noteRepository.save(nbNote);
        String[] tagNameArray = tagNames.split(",");
        saveTagNames(n.getId(), tagNameArray, tagReferRepository, tagRepository);
    }

    @Override
    public void updateNote(NBNote nbNote, String tagNames) {
        if (StringUtils.isEmpty(nbNote.getId())) {
            throw new RuntimeException("未获取到需要修改随笔的ID");
        }
        if (StringUtils.isEmpty(tagNames)) {
            throw new RuntimeException("tagName 不能为空！");
        }
        nbNote.setModify(LocalDateTime.now());
        decorateNote(nbNote);
        NBNote n = noteRepository.save(nbNote);
        if (n != null) {
            tagReferRepository.deleteByReferId(n.getId());
            String[] tagNameArray = tagNames.split(",");
            saveTagNames(n.getId(), tagNameArray, tagReferRepository, tagRepository);
        }
    }

    @Override
    public Page<NBNote> findNotePage(Pageable pageable, String title, String clearContent) {
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(clearContent)) {
            return noteRepository.findAll(pageable);
        } else {
            Example<NBNote> tagExample = Example.of(
                    NBNote.builder().clearContent(clearContent == null ? "" : clearContent).title(title == null ? "" : title).build(),
                    ExampleMatcher.matchingAny()
                            .withMatcher("clearContent", ExampleMatcher.GenericPropertyMatcher::contains)
                            .withMatcher("title", ExampleMatcher.GenericPropertyMatcher::contains)
                            .withIgnoreCase());
            return noteRepository.findAll(tagExample, pageable);
        }
    }

    /**
     * 装饰note
     *
     * @param nbNote
     */
    private static void decorateNote(NBNote nbNote) {
        if (StringUtils.isEmpty(nbNote.getPost())) {
            nbNote.setPost(LocalDateTime.now());
        }
        if (StringUtils.isEmpty(nbNote.getShow())) {
            nbNote.setShow(true);
        }
        if (StringUtils.isEmpty(nbNote.getTop())) {
            nbNote.setTop(false);
        }
    }

    /**
     * 保存note标签，最多4个
     *
     * @param noteId
     * @param tagNameArray
     * @param tagReferRepository
     * @param tagRepository
     */
    private static void saveTagNames(Long noteId, String[] tagNameArray, TagReferRepository tagReferRepository, TagRepository tagRepository) {
        int cnt = 0;
        for (String name : tagNameArray) {
            Example<NBTag> condition = Example.of(NBTag.builder().name(name).build());
            boolean isExist = tagRepository.count(condition) > 0;
            long tagId = isExist ?
                    tagRepository.findByName(name).getId() :
                    tagRepository.save(NBTag.builder().name(name).build()).getId();

            tagReferRepository.save(
                    NBTagRefer.builder()
                            .referId(noteId)
                            .tagId(tagId)
                            .show(cnt <= 4)
                            .type(TagType.note.name()).build()
            );
            cnt++;
        }
    }
}
