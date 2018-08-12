package me.wuwenbin.noteblogv4.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * created by Wuwenbin on 2018/8/3 at 23:15
 * @author wuwenbin
 */
@Slf4j
@Component
@Order
public class FinalListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("「笔记博客」App 启动完毕。讨论/反馈群：【697053454】");
    }
}
