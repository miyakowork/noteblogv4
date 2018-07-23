package me.wuwenbin.noteblogv4.service.permission;

import me.wuwenbin.noteblogv4.dao.repository.ResourceRepository;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import me.wuwenbin.noteblogv4.model.pojo.business.LayuiXTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Wuwenbin on 2018/7/20 at 14:49
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserPermissionServiceImpl implements UserPermissionService {

    private final ResourceRepository resourceRepository;

    @Autowired
    public UserPermissionServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public List<NBSysResource> getPermissionByRoleId(long roleId) {
        return null;
    }

    @Override
    public List<LayuiXTree> findResourceTreeByRoleId(long roleId) {
        List<NBSysResource> allResources = resourceRepository.findAll();
        List<NBSysResource> hasResources = resourceRepository.findAllByRoleId(roleId);
        List<LayuiXTree> resourceTree = new ArrayList<>(allResources.size());
        allResources.forEach(resource -> {
            LayuiXTree tree = LayuiXTree.builder()
                    .title(resource.getName())
                    .value(resource.getId().toString())
                    .build();
            if (hasResources.contains(resource)) {
                tree.setChecked(true);
            }
        });
        return null;
    }
}
