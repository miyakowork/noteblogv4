package me.wuwenbin.noteblogv4.repository;

import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/7/15 at 12:52
 *
 * @author wuwenbin
 */
public interface ArticleRepository extends JpaRepository<NBArticle, Long> {

}
