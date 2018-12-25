package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
     * 根据draft变量计算文章数量
     *
     * @param draft
     * @return
     */
    long countByDraft(boolean draft);

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
     * 根据文章自定义链接查找文章对象
     *
     * @param urlSeq
     * @return
     */
    Optional<NBArticle> findNBArticleByUrlSequence(String urlSeq);

    /**
     * 查找相似的文章
     *
     * @param cateId
     * @param limit
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT * FROM nb_article WHERE cate_id = ?1 ORDER BY rand() LIMIT ?2")
    List<NBArticle> findSimilarArticles(long cateId, int limit);

    /**
     * 更新文章点赞数
     *
     * @param articleId
     * @return
     * @throws Exception
     */
    @Query(nativeQuery = true, value = "UPDATE nb_article SET approve_cnt = approve_cnt + 1 WHERE id = ?1")
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    int updateApproveCntById(long articleId);

    /**
     * 更新浏览量
     *
     * @param articleId
     * @return
     * @throws Exception
     */
    @Query("UPDATE NBArticle a SET a.view = a.view + 1 WHERE a.id = ?1")
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    void updateViewsById(long articleId);


    /**
     * 更新浏览量
     *
     * @param urlSeq
     * @return
     * @throws Exception
     */
    @Query("UPDATE NBArticle a SET a.view = a.view + 1 WHERE a.urlSequence = ?1")
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    void updateViewsBySeq(String urlSeq);

    /**
     * 随机n篇文章
     *
     * @param limit
     * @return
     */
    @Query(nativeQuery = true, value = "select * from nb_article WHERE draft != 1 ORDER BY rand() LIMIT ?1 ")
    List<NBArticle> findRandomArticles(int limit);

    /**
     * 查找在此id集合中的文章集合对象
     *
     * @param articleIds
     * @param start
     * @param pageSize
     * @return
     */
    @Query(nativeQuery = true, value = "select * from nb_article where id in (?1) limit ?2,?3")
    List<NBArticle> findByIdIn(List<Long> articleIds, int start, int pageSize);
}
