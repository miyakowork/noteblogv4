package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRoleResource;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.RoleResourceKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/7/19 at 22:50
 * @author wuwenbin
 */
public interface RoleResourceRepository extends JpaRepository<NBSysRoleResource, RoleResourceKey> {

}
