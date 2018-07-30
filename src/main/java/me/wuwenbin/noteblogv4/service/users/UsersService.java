package me.wuwenbin.noteblogv4.service.users;

import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;

/**
 * created by Wuwenbin on 2018/7/29 at 1:05
 * @author wuwenbin
 */
public interface UsersService {

    /**
     * 根据条件查找用户分页数据
     *
     * @param userPage
     * @param user
     * @return
     */
    Page<NBSysUser> findUserPage(Pagination<NBSysUser> userPage, NBSysUser user);

    /**
     * 修改用户角色信息关联信息
     *
     * @param userId
     * @param roleIds
     * @return
     */
    void updateUserRoles(Long userId, String roleIds);
}
