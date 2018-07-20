package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBPanel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/7/19 at 下午3:04
 * @author wuwenbin
 */
public interface PanelRepository extends JpaRepository<NBPanel, Long> {
}
