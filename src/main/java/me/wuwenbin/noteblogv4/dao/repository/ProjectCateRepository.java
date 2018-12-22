package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBProjectCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * created by Wuwenbin on 2018/8/3 at 11:16
 *
 * @author wuwenbin
 */
public interface ProjectCateRepository extends JpaRepository<NBProjectCate, Long> {

    /**
     * 查找已存在对应的分类数目
     *
     * @param cate
     * @return
     */
    @Query("SELECT COUNT(c) FROM NBProjectCate c WHERE c.cnName = :#{#cate.cnName} AND c.name = :#{#cate.name}")
    long findCateCount(@Param("cate") NBProjectCate cate);
}
