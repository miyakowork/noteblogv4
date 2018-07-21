package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/7/16 at 14:31
 *
 * @author wuwenbin
 */
public interface RoleRepository extends JpaRepository<NBSysRole, Long> {

    /**
     * 根据角色名查找角色信息对象
     *
     * @param name
     * @return
     */
    NBSysRole findByName(String name);
}
