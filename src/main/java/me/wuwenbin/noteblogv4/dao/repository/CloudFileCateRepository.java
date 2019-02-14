package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBCloudFileCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * created by Wuwenbin on 2019/2/13 at 16:34
 *
 * @author wuwenbin
 */
public interface CloudFileCateRepository extends JpaRepository<NBCloudFileCate, Long> {

    /**
     * 查找已存在对应的分类数目
     *
     * @param cate
     * @return
     */
    @Query("SELECT COUNT(c) FROM NBCloudFileCate c WHERE c.cnName = :#{#cate.cnName} AND c.name = :#{#cate.name}")
    long findCateCount(@Param("cate") NBCloudFileCate cate);
}
