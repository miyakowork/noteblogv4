package me.wuwenbin.noteblogv4.dao.mapper;

import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import org.apache.ibatis.annotations.Param;

/**
 * created by Wuwenbin on 2018/7/29 at 0:01
 * @author wuwenbin
 */
@MybatisDao
public interface UserMapper {

    /**
     * 查找用户分页信息
     *
     * @param pagination
     * @param user
     * @return
     */
    Page<NBSysUser> findPageInfo(Pagination<NBSysUser> pagination, @Param("user") NBSysUser user);


    /**
     * 根据用户id删除相关联的角色信息
     *
     * @param userId
     * @return
     */
    void deleteRolesByUserId(Long userId);
}
