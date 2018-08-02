package me.wuwenbin.noteblogv4.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * created by Wuwenbin on 2018/8/2 at 20:35
 */
@Slf4j
@Component
@Order(3)
public class HelloWorldListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("初始化「笔记博客」APP 基本内容，请稍后...");
        setFirstCategory();
        setHelloWorldArticle();
        log.info("初始化「笔记博客」APP 基本内容完毕");
    }


    private void setFirstCategory() {
    }

    private void setHelloWorldArticle() {
    }

}
