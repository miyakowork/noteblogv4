package me.wuwenbin.noteblogv4.service.content;

import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.pojo.bo.ArticleQueryBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * created by Wuwenbin on 2018/8/5 at 20:08
 *
 * @author wuwenbin
 */
public interface ArticleService {

    /**
     * 发表一篇文章
     *
     * @param article
     * @param tagNames
     * @return
     */
    void createArticle(NBArticle article, String tagNames);

    /**
     * 修改一篇文章
     *
     * @param article
     * @param tagNames
     */
    void updateArticle(NBArticle article, String tagNames);

    /**
     * 查找文章分页信息，可带查询
     *
     * @param pageable
     * @param title
     * @param authorId
     * @return
     */
    Page<NBArticle> findPageInfo(Pageable pageable, String title, Long authorId);

    /**
     * 前端博客页面的文章分页信息
     *
     * @param pageable
     * @param articleQueryBO
     * @return
     */
    Page<NBArticle> findBlogArticles(Pageable pageable, ArticleQueryBO articleQueryBO);

    /**
     * 修改文章的 top 值
     *
     * @param articleId
     * @param top
     * @return
     * @throws Exception
     */
    boolean updateTopById(long articleId, boolean top);
}
