package me.wuwenbin.noteblogv4.service.users;

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

/**
 * created by Wuwenbin on 2018/7/29 at 1:06
 *
 * @author wuwenbin
 */
@Service
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
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
                            .withIgnoreCase());
            return userRepository.findAll(userExample, pageable);
        }
    }

    @Override
    public void updateUserRoles(Long userId, String roleIds) {
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
}
