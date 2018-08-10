package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBTagRefer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/8/8 at 16:44
 *
 * @author wuwenbin
 */
public interface TagReferRepository extends JpaRepository<NBTagRefer, Long> {

    /**
     * 根据 refer_id 删除相关的记录
     *
     * @param referId
     */
    void deleteByReferId(Long referId);
}
