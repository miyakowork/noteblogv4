package me.wuwenbin.noteblogv4.service.content;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

/**
 * created by Wuwenbin on 2018/8/5 at 20:09
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    private final DataSourceTransactionManager tm;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            DataSourceTransactionManager tm) {
        this.articleRepository = articleRepository;
        this.tm = tm;
    }

    @Override
    public boolean createArticle(NBArticle article, String tagNames, String editor) {
        if (StringUtils.isEmpty(tagNames)) {
            throw new RuntimeException("tagName不能为空！");
        }
        String[] tagNameArray = tagNames.split(",");
        DefaultTransactionDefinition def = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = tm.getTransaction(def);
        try {

            tm.commit(status);
            return true;
        } catch (Exception e) {
            tm.rollback(status);
            log.error("发布博文出错，发布失败，错误信息：{}", e);
            throw new RuntimeException(e);
        }
    }
}
