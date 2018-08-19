package me.wuwenbin.noteblogv4.service.content;

import me.wuwenbin.noteblogv4.model.entity.NBNote;

/**
 * created by Wuwenbin on 2018/8/18 at 10:32
 * @author wuwenbin
 */
public interface NoteService {

    /**
     * 新增一个笔记
     *
     * @param nbNote
     * @param tagNames
     */
    void createNote(NBNote nbNote, String tagNames);

    /**
     * 修改一篇笔记
     *
     * @param nbNote
     * @param tagNames
     */
    void updateNote(NBNote nbNote, String tagNames);
}
