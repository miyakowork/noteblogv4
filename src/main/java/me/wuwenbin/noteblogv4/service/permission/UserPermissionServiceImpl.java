package me.wuwenbin.noteblogv4.service.permission;

import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * created by Wuwenbin on 2018/7/20 at 14:49
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserPermissionServiceImpl implements UserPermissionService {
    
    @Override
    public List<NBSysResource> getPermissionByRoleId(int roleId) {
        return null;
    }
}
