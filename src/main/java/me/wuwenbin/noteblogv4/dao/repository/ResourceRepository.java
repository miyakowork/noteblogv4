package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/7/19 at 22:54
 *
 * @author wuwenbin
 */
@Transactional(rollbackOn = Exception.class)
public interface ResourceRepository extends JpaRepository<NBSysResource, Long> {

    /**
     * 查询此url的权限是否存在
     *
     * @param url
     * @return
     */
    long countByUrl(String url);

    /**
     * 更新url名称和权限标识
     *
     * @param name
     * @param permission
     * @param url
     */
    @Modifying
    @Query("update NBSysResource r set r.name = ?1, r.permission = ?2, r.type = ?3 where r.url = ?4")
    void updateByUrl(String name, String permission, NBSysResource.ResType type, String url);
}
