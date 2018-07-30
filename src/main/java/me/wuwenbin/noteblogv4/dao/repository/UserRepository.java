package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/7/18 at 17:34
 *
 * @author wuwenbin
 */
public interface UserRepository extends JpaRepository<NBSysUser, Long> {

    /**
     * 根据用户名和密码查询可用的用户
     *
     * @param username
     * @param password
     * @return
     */
    NBSysUser findByUsernameAndPasswordAndEnableTrue(String username, String password);

    /**
     * 根据用户名查找用户对象
     *
     * @param username
     * @return
     */
    NBSysUser findByUsername(String username);

    /**
     * 更新用户状态信息
     *
     * @param userId
     * @param enable
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBSysUser u set u.enable = ?2 where u.id = ?1")
    int updateUserStatus(long userId, boolean enable);
}
