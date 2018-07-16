package me.wuwenbin.noteblogv4.repository;

import me.wuwenbin.noteblogv4.model.entity.NBRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/7/16 at 14:31
 *
 * @author wuwenbin
 */
public interface RoleRepository extends JpaRepository<NBRole, Long> {
}
