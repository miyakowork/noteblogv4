package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * created by Wuwenbin on 2018/7/24 at 22:11
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

}
