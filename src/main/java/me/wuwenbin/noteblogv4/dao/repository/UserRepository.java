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
     * 根据openid查找用户
     *
     * @param qqOpenId
     * @param enable
     * @return
     */
    NBSysUser findByQqOpenIdAndEnable(String qqOpenId, boolean enable);

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

    /**
     * 修改用户昵称
     *
     * @param userId
     * @param nickname
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBSysUser u set u.nickname = ?2 where u.id = ?1")
    int updateUserNickname(long userId, String nickname);

    /**
     * 更新用户密码
     *
     * @param userId
     * @param password
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBSysUser u set u.password = ?2 where u.id = ?1")
    void updateUserPass(long userId, String password);

    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatar
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBSysUser u set u.avatar = ?2 where u.id = ?1")
    void updateUserAvatar(long userId, String avatar);

    /**
     * 更新用户邮箱
     *
     * @param userId
     * @param email
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBSysUser u set u.email = ?2 where u.id = ?1")
    void updateUserEmail(long userId, String email);

    /**
     * 根据创建日期查询用户数量
     * @param post
     * @return
     */
    @Query(nativeQuery = true,value = "select count(*) from sys_user where `create`like ?1")
    int countByCreateLike(String post);
}

