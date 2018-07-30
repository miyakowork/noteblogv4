package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBLogger;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/7/21 at 17:46
 * @author wuwenbin
 */
public interface LoggerRepository extends JpaRepository<NBLogger, Long> {

}
