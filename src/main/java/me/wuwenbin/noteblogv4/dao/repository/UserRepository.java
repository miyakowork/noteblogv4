package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/7/18 at 17:34
 *
 * @author wuwenbin
 */
public interface UserRepository extends JpaRepository<NBSysUser, Long> {

    /**
     * @param username
     * @param password
     * @return
     */
    NBSysUser findByUsernameAndPasswordAndEnableTrue(String username, String password);
}
