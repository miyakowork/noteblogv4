package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBCate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/8/3 at 11:16
 *
 * @author wuwenbin
 */
public interface CateRepository extends JpaRepository<NBCate, Long> {
}
