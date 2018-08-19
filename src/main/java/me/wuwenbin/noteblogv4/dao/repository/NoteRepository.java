package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBNote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/8/18 at 11:10
 *
 * @author wuwenbin
 */
public interface NoteRepository extends JpaRepository<NBNote, Long> {

}
