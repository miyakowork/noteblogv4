package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * created by Wuwenbin on 2018/12/25 at 17:12
 *
 * @author wuwenbin
 */
public interface MessageRepository extends JpaRepository<NBMessage, Long>, JpaSpecificationExecutor<NBMessage> {
}
