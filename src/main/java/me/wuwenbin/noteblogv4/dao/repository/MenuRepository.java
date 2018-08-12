package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * created by Wuwenbin on 2018/7/24 at 22:11
 *
 * @author wuwenbin
 */
public interface MenuRepository extends JpaRepository<NBSysMenu, Long> {

    /**
     * 查找根目录菜单
     *
     * @param parentId
     * @return
     */
    NBSysMenu findByParentId(long parentId);

    /**
     * 根据角色id查找菜单
     *
     * @param roleId
     * @return
     */
    List<NBSysMenu> findAllByRoleId(long roleId);

    /**
     * 查找数据库中是否有根菜单
     *
     * @param parentId
     * @return
     */
    long countByParentId(long parentId);

    /**
     * 查找所有菜单（包含根目录）
     *
     * @param roleId
     * @param enable
     * @return
     */
    @Query("select  m from NBSysMenu m where (m.roleId = ?1 and m.enable = ?2)or (m.roleId is null and m.parentId = 0) order by m.orderIndex asc")
    List<NBSysMenu> findAllByRoleIdOrderBy(Long roleId, boolean enable);

}
