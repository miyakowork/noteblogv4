package me.wuwenbin.noteblogv4.config.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * created by Wuwenbin on 2018/8/1 at 20:25
 * @author wuwenbin
 */
@Component
@Order(2)
public class MenuListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    }

}
