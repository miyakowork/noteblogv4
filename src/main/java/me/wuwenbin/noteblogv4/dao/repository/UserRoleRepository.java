package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUserRole;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/7/28 at 23:26
 * @author wuwenbin
 */
@Transactional(rollbackOn = Exception.class)
public interface UserRoleRepository extends JpaRepository<NBSysUserRole, UserRoleKey> {
}
