package me.wuwenbin.noteblogv4.service.users;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * created by Wuwenbin on 2018/7/29 at 1:05
 *
 * @author wuwenbin
 */
public interface UsersService {

    /**
     * 根据条件查找用户分页数据
     *
     * @param pageable
     * @param user
     * @return
     */
    Page<NBSysUser> findUserPage(Pageable pageable, NBSysUser user);

    /**
     * 修改用户角色信息关联信息
     *
     * @param userId
     * @param roleIds id形式
     * @return
     */
    void updateUserRoleIds(Long userId, String roleIds);

    /**
     * 修改用户角色信息关联信息
     *
     * @param userId
     * @param roleIds name形式
     * @return
     */
    void updateUserRolesStr(Long userId, String roleIds);
}
