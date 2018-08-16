package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/7/15 at 12:52
 *
 * @author wuwenbin
 */
public interface ArticleRepository extends JpaRepository<NBArticle, Long> {

    /**
     * 查询是否已存在自定义的url文章
     *
     * @param urlSequence
     * @return
     */
    long countByUrlSequence(String urlSequence);

    /**
     * 更新 appreciable 状态
     *
     * @param appreciable
     * @param id
     * @return
     */
    @Modifying
    @Query("update NBArticle a set a.appreciable = ?1 where a.id = ?2")
    @Transactional(rollbackOn = Exception.class)
    int updateAppreciableById(boolean appreciable, long id);

    /**
     * 更新 commented 状态
     *
     * @param commented
     * @param id
     * @return
     */
    @Modifying
    @Query("update NBArticle a set a.commented = ?1 where a.id = ?2")
    @Transactional(rollbackOn = Exception.class)
    int updateCommentedById(boolean commented, long id);

    /**
     * 更新 top 状态
     *
     * @param top
     * @param id
     * @return
     */
    @Modifying
    @Query("update NBArticle a set a.top = ?1 where a.id = ?2")
    @Transactional(rollbackOn = Exception.class)
    int updateTopById(int top, long id);

    /**
     * 处理改动 所有 top 对应值
     *
     * @param currentTop
     * @return
     */
    @Modifying
    @Query("update NBArticle a set a.top = a.top - 1 where a.top > ?1")
    @Transactional(rollbackOn = Exception.class)
    void updateTopsByTop(int currentTop);

    /**
     * 查找某个分类下文章的个数
     *
     * @param cateId
     * @return
     */
    long countByCateId(long cateId);

}
