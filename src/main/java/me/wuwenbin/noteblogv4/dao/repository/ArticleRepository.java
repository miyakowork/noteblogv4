package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/7/15 at 12:52
 *
 * @author wuwenbin
 */
public interface ArticleRepository extends JpaRepository<NBArticle, Long>, JpaSpecificationExecutor<NBArticle> {

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

    /**
     * 查找出最大的 top 值
     *
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT MAX(top)FROM nb_article ")
    int findMaxTop();

    /**
     * 查询文章分页信息
     *
     * @param title
     * @param authorId
     * @param pageable
     * @return
     */
    @Query(nativeQuery = true,
            value = "SELECT a.*,c.cn_name FROM nb_article a " +
                    "LEFT JOIN nb_cate c ON a.cate_id = c.id " +
                    "WHERE  a.title LIKE CONCAT('%', :title, '%') AND a.author_id = :authorId",
            countQuery = "SELECT COUNT(1) FROM nb_article a " +
                    "WHERE  a.title LIKE CONCAT('%', :title, '%') AND a.author_id = :authorId")
    Page<Object[]> findArticlesPageInfo(@Param("title") String title, @Param("authorId") Long authorId, Pageable pageable);
}
