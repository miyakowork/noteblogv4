package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBProject;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/12/22 at 19:12
 *
 * @author wuwenbin
 */
public interface ProjectRepository extends JpaRepository<NBProject, Long> {

    /**
     * 计算某个cateId说下的数量
     *
     * @param cateId
     * @return
     */
    long countByCateId(long cateId);
}
