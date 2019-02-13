package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRoleResource;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.RoleResourceKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
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
    @Query(nativeQuery = true, value = "SELECT CAST( resource_id AS SIGNED ) FROM sys_role_resource WHERE role_id = :roleId")
    List<Long> findResourceIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除角色资源关系
     *
     * @param roleId
     */
    @Query(nativeQuery = true, value = "DELETE FROM sys_role_resource WHERE role_id = ?1")
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    void deleteRrByRoleId(long roleId);
}
