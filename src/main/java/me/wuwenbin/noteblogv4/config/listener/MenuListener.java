package me.wuwenbin.noteblogv4.config.listener;

import me.wuwenbin.noteblogv4.dao.repository.MenuRepository;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * created by Wuwenbin on 2018/8/1 at 20:25
 *
 * @author wuwenbin
 */
@Component
@Order(2)
public class MenuListener implements ApplicationListener<ApplicationReadyEvent> {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuListener(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    }

    /**
     * 查找root 菜单的 id
     *
     * @return
     */
    private long findRootId() {
        return menuRepository.findByType(NBSysMenu.MenuType.ROOT).getId();
    }
}
