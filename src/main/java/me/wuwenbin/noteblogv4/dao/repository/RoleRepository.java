package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    /**
     * 根据用户id查询该用户的所有角色
     *
     * @param userId
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT r.* FROM sys_role r WHERE r.id IN " +
            "(SELECT role_id FROM sys_user_role WHERE user_id = ?1)")
    List<NBSysRole> findUserRoleIds(Long userId);
}
