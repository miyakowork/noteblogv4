package me.wuwenbin.noteblogv4.service.content;

import me.wuwenbin.noteblogv4.model.entity.NBArticle;

/**
 * created by Wuwenbin on 2018/8/5 at 20:08
 */
public interface ArticleService {

    /**
     * 发表一篇文章
     *
     * @param article
     * @param tagNames
     * @param editor
     * @return
     */
    boolean createArticle(NBArticle article, String tagNames, String editor);
}
