package me.wuwenbin.noteblogv4.service.users;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import me.wuwenbin.noteblogv4.dao.mapper.UserMapper;
import me.wuwenbin.noteblogv4.dao.repository.UserRoleRepository;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUserRole;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.UserRoleKey;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by Wuwenbin on 2018/7/29 at 1:06
 */
@Service
public class UsersServiceImpl implements UsersService {

    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UsersServiceImpl(UserMapper userMapper, UserRoleRepository userRoleRepository) {
        this.userMapper = userMapper;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Page<NBSysUser> findUserPage(Pagination<NBSysUser> userPage, NBSysUser user) {
        PageHelper.startPage(userPage.getPage(), userPage.getLimit(), userPage.getOrderBy());
        return userMapper.findPageInfo(userPage, user);
    }

    @Override
    public void updateUserRoles(Long userId, String roleIds) {
        userMapper.deleteRolesByUserId(userId);
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
