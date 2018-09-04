package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

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
     * @param type
     * @param group
     * @param url
     */
    @Modifying
    @Query("update NBSysResource r set r.name = ?1, r.permission = ?2, r.type = ?3, r.group = ?4 where r.url = ?5")
    void updateByUrl(String name, String permission, NBSysResource.ResType type, String group, String url);

    /**
     * 根据资源类型查找
     *
     * @param type
     * @param ids
     * @return
     */
    @Query("SELECT r FROM NBSysResource r WHERE r.type = ?1 AND r.id IN (?2)")
    List<NBSysResource> findAllByTypeAndIdIn(NBSysResource.ResType type, List<Long> ids);

    /**
     * 根据url查找
     *
     * @param url
     * @return
     */
    NBSysResource findByUrl(String url);


    /**
     * 根据角色id查找资源
     *
     * @param roleId
     * @return
     */
    @Query(nativeQuery = true, value = " SELECT r.* FROM sys_resource r WHERE r.id IN" +
            " (SELECT rr.resource_id FROM sys_role_resource rr WHERE rr.role_id = ?1)")
    List<NBSysResource> findResourcesByRoleId(long roleId);
}
