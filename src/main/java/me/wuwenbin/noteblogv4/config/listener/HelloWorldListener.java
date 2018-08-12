package me.wuwenbin.noteblogv4.config.listener;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * created by Wuwenbin on 2018/8/2 at 20:35
 * @author wuwenbin
 */
@Slf4j
@Component
@Order(4)
public class HelloWorldListener implements ApplicationListener<ApplicationReadyEvent> {

    private final CateRepository cateRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public HelloWorldListener(CateRepository cateRepository, ArticleRepository articleRepository) {
        this.cateRepository = cateRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (cateRepository.count() == 0 && articleRepository.count() == 0) {
            log.info("初始化「笔记博客」APP 基本内容，请稍后...");
            setFirstCategory();
            setHelloWorldArticle();
            log.info("初始化「笔记博客」APP 基本内容完毕");
        }
    }


    private void setFirstCategory() {
    }

    private void setHelloWorldArticle() {
    }

}
