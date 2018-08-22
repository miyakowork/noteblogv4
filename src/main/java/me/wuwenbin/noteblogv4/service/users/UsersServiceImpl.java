package me.wuwenbin.noteblogv4.service.users;

import me.wuwenbin.noteblogv4.dao.repository.RoleRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRoleRepository;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUserRole;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.UserRoleKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/7/29 at 1:06
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<NBSysUser> findUserPage(Pageable pageable, NBSysUser user) {
        if (StringUtils.isEmpty(user.getNickname()) && StringUtils.isEmpty(user.getUsername())) {
            return userRepository.findAll(pageable);
        } else {
            Example<NBSysUser> userExample = Example.of(
                    NBSysUser.builder()
                            .nickname(user.getNickname() == null ? "" : user.getNickname())
                            .username(user.getUsername() == null ? "" : user.getUsername())
                            .build(),
                    ExampleMatcher.matching()
                            .withMatcher("nickname", ExampleMatcher.GenericPropertyMatcher::contains)
                            .withMatcher("username", ExampleMatcher.GenericPropertyMatcher::contains)
                            .withIgnorePaths("enable", "defaultRoleId", "create")
                            .withIgnoreCase());
            return userRepository.findAll(userExample, pageable);
        }
    }

    @Override
    public void updateUserRoleIds(Long userId, String roleIds) {
        userRoleRepository.deleteRolesByUserId(userId);
        String[] roleIdArray = roleIds.split(",");
        for (String roleId : roleIdArray) {
            long rId = Long.parseLong(roleId);
            UserRoleKey urk = new UserRoleKey();
            urk.setUserId(userId);
            urk.setRoleId(rId);
            NBSysUserRole ur = NBSysUserRole.builder().pk(urk).build();
            userRoleRepository.saveAndFlush(ur);
        }
    }

    @Override
    public void updateUserRolesStr(Long userId, String roleNames) {
        userRoleRepository.deleteRolesByUserId(userId);
        String[] roleNameArray = roleNames.split(",");
        for (String roleName : roleNameArray) {
            long rId = roleRepository.findByName(roleName).getId();
            UserRoleKey urk = new UserRoleKey();
            urk.setUserId(userId);
            urk.setRoleId(rId);
            NBSysUserRole ur = NBSysUserRole.builder().pk(urk).build();
            userRoleRepository.saveAndFlush(ur);
        }
    }
}
