package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

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

    /**
     * 根据id更新project相关内容
     *
     * @param project
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "update nb_project " +
            "set cate_id = #{#project.cateId},cover=#{#project.cover},description=#{#project.description},name=#{#project.name},url=#{#project.url},cate_refer_id=#{#project.cateId} " +
            "where id=#{#project.id}")
    @Transactional(rollbackOn = Exception.class)
    int updateProjectById(@Param("project") NBProject project);
}
