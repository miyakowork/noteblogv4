package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUserRole;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/7/28 at 23:26
 *
 * @author wuwenbin
 */
@Transactional(rollbackOn = Exception.class)
public interface UserRoleRepository extends JpaRepository<NBSysUserRole, UserRoleKey> {

    /**
     * 根据用户id删除相关联的角色信息
     *
     * @param userId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM sys_user_role WHERE user_id = ?1")
    void deleteRolesByUserId(Long userId);
}
