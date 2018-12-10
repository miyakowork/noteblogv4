package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBAbout;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/12/8 at 23:05
 *
 * @author wuwenbin
 */
public interface ProfileRepository extends JpaRepository<NBAbout, Long> {
}
