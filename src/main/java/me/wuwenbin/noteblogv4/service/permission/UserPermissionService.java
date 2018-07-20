package me.wuwenbin.noteblogv4.service.permission;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;

import java.util.List;

/**
 * created by Wuwenbin on 2018/7/20 at 14:48
 *
 * @author wuwenbin
 */
public interface UserPermissionService {

    /**
     * 查询该角色所包含的所有权限
     *
     * @param roleId
     * @return
     */
    List<NBSysResource> getPermissionByRoleId(int roleId);
}
