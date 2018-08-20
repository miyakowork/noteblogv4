package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRoleResource;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.RoleResourceKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * created by Wuwenbin on 2018/7/19 at 22:50
 *
 * @author wuwenbin
 */
public interface RoleResourceRepository extends JpaRepository<NBSysRoleResource, RoleResourceKey> {

    /**
     * 根据roleId查找相关的resourceId
     *
     * @param roleId
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT resource_id FROM sys_role_resource WHERE role_id = :roleId")
    List<Long> findResourceIdByRoleId(@Param("roleId") Long roleId);
}
