package me.wuwenbin.noteblogv4.web.management.authority;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.config.permission.NBAuth.Group;
import me.wuwenbin.noteblogv4.dao.repository.MenuRepository;
import me.wuwenbin.noteblogv4.dao.repository.ResourceRepository;
import me.wuwenbin.noteblogv4.dao.repository.RoleRepository;
import me.wuwenbin.noteblogv4.dao.repository.RoleResourceRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRole;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRoleResource;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.RoleResourceKey;
import me.wuwenbin.noteblogv4.model.pojo.framework.LayuiTable;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.service.authority.AuthorityService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
public class AuthorityController extends BaseController {

    private final RoleRepository roleRepository;
    private final AuthorityService authorityService;
    private final RoleResourceRepository roleResourceRepository;
    private final MenuRepository menuRepository;
    private final ResourceRepository resourceRepository;

    @Autowired
    public AuthorityController(RoleRepository rr, AuthorityService ups,
                               RoleResourceRepository rrr,
                               MenuRepository mr, ResourceRepository resR) {
        this.roleRepository = rr;
        this.authorityService = ups;
        this.roleResourceRepository = rrr;
        this.menuRepository = mr;
        this.resourceRepository = resR;
    }


    @RequestMapping("/role")
    @NBAuth(value = "permission:role:router", remark = "后台角色管理页面", type = ResType.NAV_LINK, group = Group.ROUTER)
    public String roleRouter(Model model) {
        List<NBSysRole> roles = roleRepository.findAll();
        model.addAttribute("roleList", roles);
        return "management/authority/role";
    }


    @RequestMapping("/menu")
    @NBAuth(value = "permission:menu:router", remark = "菜单管理页面", type = ResType.NAV_LINK, group = Group.ROUTER)
    public String menuIndex(Model model, Long roleId) {
        List<NBSysRole> roles = roleRepository.findAll();
        List<NBSysMenu> menus = menuRepository.findAll();
        model.addAttribute("roleList", roles);
        model.addAttribute("menuList", menus);
        if (!StringUtils.isEmpty(roleId)) {
            model.addAttribute("roleId", roleId);
        }
        return "management/authority/menu";
    }


    @RequestMapping("/resource/tree")
    @ResponseBody
    @NBAuth(value = "permission:role:resource_tree", remark = "后台角色管理页面的资源树", group = Group.AJAX)
    public NBR resourcesTree(Long roleId) {
        return NBR.ok(authorityService.findResourceTreeByRoleId(roleId));
    }


    @RequestMapping("/update/role/resource")
    @ResponseBody
    @NBAuth(value = "permission:role:update_role_resource", remark = "更新角色所拥有的资源信息", group = Group.AJAX)
    public NBR updateRoleResource(Long roleId, @RequestParam(value = "resourceIds[]", required = false) Long[] resourceIds) {
        roleResourceRepository.deleteRrByRoleId(roleId);
        if (resourceIds != null && resourceIds.length > 0) {
            for (Long resource : resourceIds) {
                if (!StringUtils.isEmpty(resource)) {
                    NBSysRoleResource rr = NBSysRoleResource.builder()
                            .pk(new RoleResourceKey(roleId, resource)).build();
                    roleResourceRepository.saveAndFlush(rr);
                }
            }
        }
        return NBR.ok("更新角色资源权限成功！");
    }


    @ResponseBody
    @RequestMapping("/role/create")
    @NBAuth(value = "permission:role:create", remark = "添加新角色操作", group = Group.AJAX)
    public NBR addRole(@Valid NBSysRole role, BindingResult result) {
        if (result.getErrorCount() == 0) {
            roleRepository.saveAndFlush(role);
            return NBR.formatOk("添加角色 [{}] 成功！", role.getCnName());
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @ResponseBody
    @RequestMapping("/role/delete")
    @NBAuth(value = "permission:role:delete", remark = "删除角色操作", group = Group.AJAX)
    public NBR deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
        return NBR.ok("删除角色成功！");
    }


    @ResponseBody
    @RequestMapping("/role/update")
    @NBAuth(value = "permission:role:update", remark = "修改角色操作", group = Group.AJAX)
    public NBR updateRole(@Valid NBSysRole role, BindingResult result) {
        if (result.getErrorCount() == 0) {
            roleRepository.saveAndFlush(role);
            return NBR.formatOk("修改角色 [{}] 成功！", role.getCnName());
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @RequestMapping("/menu/list")
    @ResponseBody
    @NBAuth(value = "permission:menu:table_list", remark = "菜单管理界面的菜单数据", group = Group.AJAX)
    public LayuiTable<NBSysMenu> roleMenuList(Long roleId) {
        List<NBSysMenu> menus = menuRepository.findAllByRoleId(roleId);
        menus.add(menuRepository.findByParentId(0L));
        return new LayuiTable<>(menus.size(), menus);
    }


    @RequestMapping("/menu/add")
    @NBAuth(value = "permission:menu:add", remark = "添加角色菜单界面", group = Group.ROUTER)
    public String addMenu(Model model, Long roleId, String parentId) {
        if (StringUtils.isEmpty(roleId)) {
            return NoteBlogV4.Session.ERROR_ROUTER;
        }
        model.addAttribute("roleId", roleId);
        model.addAttribute("parentId", parentId);
        List<Long> resIds = roleResourceRepository.findResourceIdByRoleId(roleId);
        if (resIds != null && resIds.size() > 0) {
            model.addAttribute("resources", resourceRepository.findAllByTypeAndIdIn(ResType.NAV_LINK, resIds));
        }
        return "management/authority/menu_add";
    }


    @RequestMapping("/menu/edit")
    @NBAuth(value = "permission:menu:edit", remark = "修改角色菜单界面", group = Group.ROUTER)
    public String addMenu(Model model, Long menuId, Long roleId) {
        if (StringUtils.isEmpty(menuId) || StringUtils.isEmpty(roleId)) {
            return NoteBlogV4.Session.ERROR_ROUTER;
        }
        List<Long> resIds = roleResourceRepository.findResourceIdByRoleId(roleId);
        if (resIds != null && resIds.size() > 0) {
            model.addAttribute("resources", resourceRepository.findAllByTypeAndIdIn(ResType.NAV_LINK, resIds));
        }
        model.addAttribute("menu", menuRepository.getOne(menuId));
        return "management/authority/menu_edit";
    }


    @ResponseBody
    @NBAuth(value = "permission:menu:create", remark = "添加新角色菜单操作", group = Group.AJAX)
    @RequestMapping("/menu/create")
    public NBR createMenu(@Valid NBSysMenu menu, BindingResult result) {
        if (result.getErrorCount() == 0) {
            menuRepository.saveAndFlush(menu);
            return NBR.formatOk("添加菜单 [{}] 成功！", menu.getName());
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @ResponseBody
    @NBAuth(value = "permission:menu:update", remark = "修改角色菜单", group = Group.AJAX)
    @RequestMapping("/menu/update")
    public NBR modifyMenu(@Valid NBSysMenu menu, BindingResult result) {
        if (result.getErrorCount() == 0) {
            if (StringUtils.isEmpty(menu.getId())) {
                return NBR.error("id不能为空！");
            }
            menuRepository.saveAndFlush(menu);
            return NBR.formatOk("修改菜单 [{}] 成功！", menu.getName());
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @ResponseBody
    @NBAuth(value = "permission:menu:delete", remark = "删除角色菜单", group = Group.AJAX)
    @RequestMapping("/menu/delete")
    public NBR deleteMenu(Long id) {
        authorityService.deleteMenu(id);
        return NBR.ok("删除菜单成功！");
    }


}
