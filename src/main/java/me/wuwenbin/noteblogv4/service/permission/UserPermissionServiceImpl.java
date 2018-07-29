package me.wuwenbin.noteblogv4.service.permission;

import cn.hutool.crypto.SecureUtil;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.dao.mapper.PermissionMapper;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.dao.repository.ResourceRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRoleRepository;
import me.wuwenbin.noteblogv4.exception.InitException;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUserRole;
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
public class UserPermissionServiceImpl implements UserPermissionService {

    private final ResourceRepository resourceRepository;
    private final PermissionMapper permissionMapper;
    private final UserRepository userRepository;
    private final ParamRepository paramRepository;
    private final UserRoleRepository userRoleRepository;
    private final NBContext blogContext;

    @Autowired
    public UserPermissionServiceImpl(ResourceRepository resourceRepository,
                                     PermissionMapper permissionMapper, NBContext blogContext, UserRepository userRepository, ParamRepository paramRepository, UserRoleRepository userRoleRepository) {
        this.resourceRepository = resourceRepository;
        this.permissionMapper = permissionMapper;
        this.blogContext = blogContext;
        this.userRepository = userRepository;
        this.paramRepository = paramRepository;
        this.userRoleRepository = userRoleRepository;
    }


    @Override
    public List<LayuiXTree> findResourceTreeByRoleId(long roleId) {
        List<NBSysResource> all = resourceRepository.findAll();
        List<NBSysResource> allGroup = permissionMapper.findAllGroupCates();
        List<NBSysResource> hasResources = permissionMapper.findResourcesByRoleId(roleId);
        List<LayuiXTree> treeList = new ArrayList<>(all.size());
        treeList.addAll(transTo(allGroup, NBSysResource::getGroup, NBSysResource::getGroup, hasResources::contains, res -> false));
        treeList.forEach(tree -> {
            List<NBSysResource> resources = resourceRepository.findAllByGroup(tree.getValue());
            tree.setData(transTo(resources, NBSysResource::getName, res -> res.getId().toString(), hasResources::contains, res -> false));
        });
        return treeList;
    }

    @Override
    public void initMasterAccount(String username, String password, String email) {
        Long masterRoleId = blogContext.getApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID);
        if (masterRoleId == null) {
            throw new InitException("初始化权限系统出错，管理员角色未初始化！");
        }
        String nickname = "user".concat(String.valueOf(System.currentTimeMillis()));
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
            UserRoleKey urk = new UserRoleKey();
            urk.setRoleId(masterRoleId);
            urk.setUserId(u.getId());
            userRoleRepository.saveAndFlush(NBSysUserRole.builder().pk(urk).build());
        }
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
                                     Function<NBSysResource, String> value,
                                     Function<NBSysResource, Boolean> checked,
                                     Function<NBSysResource, Boolean> disabled) {

        List<LayuiXTree> treeList = new ArrayList<>();
        data.forEach(res -> {
            LayuiXTree tree = LayuiXTree.builder()
                    .title(title.apply(res))
                    .value(value.apply(res))
                    .checked(checked.apply(res))
                    .disabled(disabled.apply(res))
                    .build();
            treeList.add(tree);
        });
        return treeList;
    }
}
