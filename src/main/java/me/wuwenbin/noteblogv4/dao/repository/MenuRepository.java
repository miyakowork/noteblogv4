package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu.MenuType;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
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
     * 查找某个菜单下的所有子菜单
     *
     * @param parentId
     * @return
     */
    List<NBSysMenu> findAllByParentId(long parentId);

    /**
     * 查找数据库中是否有根菜单
     *
     * @param parentId
     * @return
     */
    long countByParentId(long parentId);

    /**
     * 根据类型查找菜单数量
     *
     * @param type
     * @return
     */
    @Query(nativeQuery = true, value = "select count(*) from sys_menu where type = ?1")
    long countByType(String type);

    /**
     * 查找所有菜单（包含根目录）
     *
     * @param roleId
     * @param enable
     * @return
     */
    @Query("select  m from NBSysMenu m where (m.roleId = ?1 and m.enable = ?2)or (m.roleId is null and m.parentId = 0) order by m.orderIndex asc")
    List<NBSysMenu> findAllByRoleIdOrderBy(Long roleId, boolean enable);

    /**
     * 根据菜单类型查找对象
     *
     * @param type
     * @return
     */
    NBSysMenu findByType(MenuType type);

    /**
     * 更新菜单的资源以及备注
     *
     * @param id
     * @param remark
     * @param resource
     */
    @Modifying
    @Query("update NBSysMenu m set m.remark = ?2, m.resource = ?3 where m.id= ?1")
    @Transactional(rollbackOn = Exception.class)
    void updateResourceById(Long id, String remark, NBSysResource resource);

    /**
     * 删除某个节点下的所有子菜单
     *
     * @param parentId
     */
    void deleteAllByParentId(Long parentId);

    /**
     * 根据resource_id查找菜单
     *
     * @param id
     * @return
     */
    @Query(nativeQuery = true, value = "select * from sys_menu where resource_id = ?1")
    NBSysMenu findByResourceId(Long id);

    /**
     * 更新菜单状态
     *
     * @param enable
     * @param id
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    @Query(nativeQuery = true, value = "update sys_menu set enable = ?1 where id = ?2")
    @Modifying
    void updateEnableById(boolean enable, Long id);

    /**
     * 更新权限菜单目录
     *
     * @param enable
     * @return
     */
    @Query(nativeQuery = true, value = "update sys_menu set enable = ?1 where name = '权限管理' and type = 'PARENT'")
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    void updateAuthParentMenu(boolean enable);
}
