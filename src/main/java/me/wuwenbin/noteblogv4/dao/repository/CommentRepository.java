package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * created by Wuwenbin on 2018/9/6 at 15:54
 *
 * @author wuwenbin
 */
public interface CommentRepository extends JpaRepository<NBComment, Long>, JpaSpecificationExecutor<NBComment> {
}
