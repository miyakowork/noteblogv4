package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBTagRefer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    /**
     * 查找是否有内容使用此标签，返回使用次数
     *
     * @param tagId
     * @return
     */
    @Query("SELECT count(tr) FROM NBTagRefer tr WHERE tr.tagId = ?1")
    long countByTagId(Long tagId);
}
