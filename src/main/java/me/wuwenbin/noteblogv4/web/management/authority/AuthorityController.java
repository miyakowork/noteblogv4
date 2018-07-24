package me.wuwenbin.noteblogv4.web.management.authority;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.mapper.UserPermissionMapper;
import me.wuwenbin.noteblogv4.dao.repository.MenuRepository;
import me.wuwenbin.noteblogv4.dao.repository.RoleRepository;
import me.wuwenbin.noteblogv4.dao.repository.RoleResourceRepository;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRole;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRoleResource;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.RoleResourceKey;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.service.permission.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * created by Wuwenbin on 2018/7/23 at 10:50
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class AuthorityController {

    private final RoleRepository roleRepository;
    private final UserPermissionService userPermissionService;
    private final RoleResourceRepository roleResourceRepository;
    private final UserPermissionMapper userPermissionMapper;
    private final MenuRepository menuRepository;

    @Autowired
    public AuthorityController(RoleRepository roleRepository,
                               UserPermissionService userPermissionService,
                               RoleResourceRepository roleResourceRepository,
                               UserPermissionMapper userPermissionMapper, MenuRepository menuRepository) {
        this.roleRepository = roleRepository;
        this.userPermissionService = userPermissionService;
        this.roleResourceRepository = roleResourceRepository;
        this.userPermissionMapper = userPermissionMapper;
        this.menuRepository = menuRepository;
    }

    /**
     * 后台角色管理页面
     *
     * @param model
     * @return
     */
    @NBAuth(value = "permission:role:router", remark = "角色管理页面", type = ResType.NAV_LINK, group = "permission")
    @RequestMapping("/role")
    public String roleIndex(Model model) {
        List<NBSysRole> roles = roleRepository.findAll();
        model.addAttribute("roleList", roles);
        return "management/authority/role";
    }

    /**
     * 角色菜单管理页面
     *
     * @param model
     * @return
     */
    @NBAuth(value = "permission:menu:router", remark = "菜单管理页面", type = ResType.NAV_LINK, group = "permission")
    @RequestMapping("/menu")
    public String menuIndex(Model model) {
        List<NBSysRole> roles = roleRepository.findAll();
        List<NBSysMenu> menus = menuRepository.findAll();
        model.addAttribute("roleList", roles);
        model.addAttribute("menuList", menus);
        return "management/authority/menu";
    }

    /**
     * 后台角色管理页面的资源树请求地址
     *
     * @param roleId
     * @return
     */
    @NBAuth(value = "permission:resourceTree:ajax", remark = "后台角色管理页面的资源树请求地址", group = "permission")
    @RequestMapping("/resource/tree")
    @ResponseBody
    public NBR resourcesTree(Long roleId) {
        return NBR.ok(userPermissionService.findResourceTreeByRoleId(roleId));
    }

    /**
     * 更新角色所拥有的资源信息
     *
     * @param roleId
     * @param resourceIds
     * @return
     */
    @NBAuth(value = "permission:updateRoleResource:ajax", remark = "更新角色所拥有的资源信息", group = "permission")
    @RequestMapping("/update/role/resource")
    @ResponseBody
    public NBR updateRoleResource(Long roleId, @RequestParam(value = "resourceIds[]", required = false) Long[] resourceIds) {
        userPermissionMapper.deleteRrByRoleId(roleId);
        if (resourceIds != null && resourceIds.length > 0) {
            for (long resource : resourceIds) {
                NBSysRoleResource rr = NBSysRoleResource.builder()
                        .pk(new RoleResourceKey(roleId, resource)).build();
                roleResourceRepository.saveAndFlush(rr);
            }
        }
        return NBR.ok("更新角色资源权限成功！");
    }

    /**
     * 添加新角色操作
     *
     * @param role
     * @param result
     * @return
     */
    @NBAuth(value = "permission:roleAdd:ajax", remark = "添加新角色操作", group = "permission")
    @ResponseBody
    @RequestMapping("/role/add")
    public NBR addRole(@Valid NBSysRole role, BindingResult result) {
        if (result.getErrorCount() == 0) {
            roleRepository.saveAndFlush(role);
            return NBR.formatOk("添加角色 [{}] 成功！", role.getCnName());
        } else {
            return NBR.error(result.getAllErrors().toString());
        }
    }

    /**
     * 删除角色操作
     *
     * @param roleId
     * @return
     */
    @NBAuth(value = "permission:roleDelete:ajax", remark = "删除角色操作", group = "permission")
    @ResponseBody
    @RequestMapping("/role/delete")
    public NBR deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
        return NBR.ok("删除角色成功！");
    }


    /**
     * 修改角色操作
     *
     * @param role
     * @param result
     * @return
     */
    @NBAuth(value = "permission:roleModify:ajax", remark = "修改角色操作", group = "permission")
    @ResponseBody
    @RequestMapping("/role/update")
    public NBR updateRole(@Valid NBSysRole role, BindingResult result) {
        if (result.getErrorCount() == 0) {
            roleRepository.saveAndFlush(role);
            return NBR.formatOk("修改角色 [{}] 成功！", role.getCnName());
        } else {
            return NBR.error(result.getAllErrors().toString());
        }
    }

    /**
     * 菜单管理界面
     *
     * @param roleId
     * @return
     */
    @NBAuth(value = "permission:menu:router", remark = "菜单管理界面", group = "permission", type = ResType.NAV_LINK)
    @RequestMapping("/menu/list")
    @ResponseBody
    public LayuiTable<NBSysMenu> roleMenuList(Long roleId) {
        List<NBSysMenu> menus = menuRepository.findAllByRoleId(roleId);
        menus.add(menuRepository.findByParentId(0L));
        return new LayuiTable<>(menus.size(), menus);
    }


}
