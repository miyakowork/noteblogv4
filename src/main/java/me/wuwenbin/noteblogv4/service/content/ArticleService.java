package me.wuwenbin.noteblogv4.service.content;

import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.model.pojo.vo.NBArticleVO;

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
     * 查找文章分页信息，可带查询
     *
     * @param articlePage
     * @param title
     * @return
     */
    Page<NBArticleVO> findPageInfo(Pagination<NBArticleVO> articlePage, String title);

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
