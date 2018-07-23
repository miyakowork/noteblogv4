package me.wuwenbin.noteblogv4.web.management.authority;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.mapper.UserPermissionMapper;
import me.wuwenbin.noteblogv4.dao.repository.RoleRepository;
import me.wuwenbin.noteblogv4.dao.repository.RoleResourceRepository;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRole;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRoleResource;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.RoleResourceKey;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.service.permission.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    public AuthorityController(RoleRepository roleRepository,
                               UserPermissionService userPermissionService,
                               RoleResourceRepository roleResourceRepository,
                               UserPermissionMapper userPermissionMapper) {
        this.roleRepository = roleRepository;
        this.userPermissionService = userPermissionService;
        this.roleResourceRepository = roleResourceRepository;
        this.userPermissionMapper = userPermissionMapper;
    }

    /**
     * 后台角色管理页面
     *
     * @param model
     * @return
     */
    @NBAuth(value = "permission:role:router", remark = "后台角色管理页面", type = ResType.NAV_LINK, group = "permission")
    @RequestMapping("/role")
    public String roleIndex(Model model) {
        List<NBSysRole> roles = roleRepository.findAll();
        model.addAttribute("roleList", roles);
        return "management/authority/role";
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

}
