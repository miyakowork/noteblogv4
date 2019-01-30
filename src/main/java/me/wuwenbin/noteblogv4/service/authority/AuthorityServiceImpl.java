package me.wuwenbin.noteblogv4.service.authority;

import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.dao.repository.*;
import me.wuwenbin.noteblogv4.exception.InitException;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.permission.*;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.UserRoleKey;
import me.wuwenbin.noteblogv4.model.pojo.business.LayuiXTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * created by Wuwenbin on 2018/7/20 at 14:49
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class AuthorityServiceImpl implements AuthorityService {

    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final ParamRepository paramRepository;
    private final UserRoleRepository userRoleRepository;
    private final MenuRepository menuRepository;
    private final NBContext blogContext;
    private final RoleRepository roleRepository;


    @Autowired
    public AuthorityServiceImpl(ResourceRepository resourceRepository,
                                NBContext blogContext, UserRepository userRepository,
                                ParamRepository paramRepository, UserRoleRepository userRoleRepository,
                                MenuRepository menuRepository, RoleRepository roleRepository) {
        this.resourceRepository = resourceRepository;
        this.blogContext = blogContext;
        this.userRepository = userRepository;
        this.paramRepository = paramRepository;
        this.userRoleRepository = userRoleRepository;
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<LayuiXTree> findResourceTreeByRoleId(long roleId) {
        List<NBSysResource> all = resourceRepository.findAll();
        List<NBSysResource> hasResources = resourceRepository.findResourcesByRoleId(roleId);
        List<LayuiXTree> treeList = new ArrayList<>(all.size());
        treeList.addAll(transTo(all, NBSysResource::getName, NBSysResource::getId, NBSysResource::getPermission, hasResources::contains, res -> false));
        return LayuiXTree.buildByRecursive(treeList);
    }

    @Override
    public void initMasterAccount(String username, String password, String email) {
        Long masterRoleId = blogContext.getApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID);
        if (masterRoleId == null) {
            throw new InitException("初始化权限系统出错，管理员角色未初始化！");
        }
        String nickname = "nbv4_user";
        NBSysUser user = NBSysUser.builder()
                .defaultRoleId(masterRoleId)
                .nickname(nickname)
                .password(SecureUtil.md5(password))
                .username(username)
                .avatar("/static/assets/img/bmy.png")
                .email(email)
                .build();
        NBSysUser u = userRepository.saveAndFlush(user);
        if (u != null) {
            paramRepository.updateInitParam("1", "is_set_master");
            paramRepository.updateValueByName(NoteBlogV4.Param.MAIL_SENDER_NAME, u.getNickname());
            UserRoleKey urk = new UserRoleKey();
            urk.setRoleId(masterRoleId);
            urk.setUserId(u.getId());
            userRoleRepository.saveAndFlush(NBSysUserRole.builder().pk(urk).build());
        }
    }

    @Override
    public void deleteMenu(Long menuId) {
        List<NBSysMenu> sonMenus = menuRepository.findAllByParentId(menuId);
        for (NBSysMenu sm : sonMenus) {
            menuRepository.deleteAllByParentId(sm.getId());
        }
        menuRepository.deleteAllByParentId(menuId);
        menuRepository.deleteById(menuId);
    }

    @Override
    public void userRegistration(String nickname, String pass, String username) {
        NBSysRole normalUserRole = roleRepository.findByName("ROLE_USER");
        NBSysUser saveUser = NBSysUser.builder()
                .defaultRoleId(normalUserRole.getId())
                .nickname(nickname)
                .avatar("/static/assets/img/favicon.png")
                .password(SecureUtil.md5(pass))
                .username(username).build();
        userRepository.saveAndFlush(saveUser);
        UserRoleKey urk = new UserRoleKey();
        urk.setRoleId(normalUserRole.getId());
        urk.setUserId(saveUser.getId());
        userRoleRepository.saveAndFlush(NBSysUserRole.builder().pk(urk).enable(true).build());
    }

    /**
     * 数据库模型转LayuiXTree
     *
     * @param data
     * @param title
     * @param value
     * @param checked
     * @param disabled
     * @return
     */
    private List<LayuiXTree> transTo(List<NBSysResource> data,
                                     Function<NBSysResource, String> title,
                                     Function<NBSysResource, Long> value,
                                     Function<NBSysResource, String> group,
                                     Function<NBSysResource, Boolean> checked,
                                     Function<NBSysResource, Boolean> disabled) {

        List<LayuiXTree> treeList = new ArrayList<>();
        data.forEach(res -> {
            LayuiXTree tree = new LayuiXTree(
                    title.apply(res),
                    value.apply(res).toString(),
                    group.apply(res),
                    checked.apply(res),
                    disabled.apply(res));
            treeList.add(tree);
        });
        return treeList;
    }
}