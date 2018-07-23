package me.wuwenbin.noteblogv4.dao.mapper;

import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by Wuwenbin on 2018/7/20 at 14:52
 *
 * @author wuwenbin
 */
@MybatisDao
public interface UserPermissionMapper {

    /**
     * 根据角色id查找资源
     *
     * @param roleId
     * @return
     */
    List<NBSysResource> findResourcesByRoleId(@Param("roleId") long roleId);

    /**
     * 查询所有group（不重复）
     *
     * @return
     */
    List<NBSysResource> findAllGroupCates();


    /**
     * 删除角色资源关系
     *
     * @param roleId
     */
    void deleteRrByRoleId(long roleId);
}
