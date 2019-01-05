package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/12/25 at 17:12
 *
 * @author wuwenbin
 */
public interface MessageRepository extends JpaRepository<NBMessage, Long>, JpaSpecificationExecutor<NBMessage> {

    /**
     * 更新消息状态
     *
     * @param id
     * @param enable
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBMessage m set m.enable = ?2 where m.id = ?1")
    int updateMessageStatus(Long id, boolean enable);
}
