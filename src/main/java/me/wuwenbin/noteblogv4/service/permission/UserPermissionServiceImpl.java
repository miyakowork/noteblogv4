package me.wuwenbin.noteblogv4.service.permission;

import me.wuwenbin.noteblogv4.dao.mapper.UserPermissionMapper;
import me.wuwenbin.noteblogv4.dao.repository.ResourceRepository;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
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
    private final UserPermissionMapper userPermissionMapper;

    @Autowired
    public UserPermissionServiceImpl(ResourceRepository resourceRepository,
                                     UserPermissionMapper userPermissionMapper) {
        this.resourceRepository = resourceRepository;
        this.userPermissionMapper = userPermissionMapper;
    }


    @Override
    public List<LayuiXTree> findResourceTreeByRoleId(long roleId) {
        List<NBSysResource> all = resourceRepository.findAll();
        List<NBSysResource> allGroup = userPermissionMapper.findAllGroupCates();
        List<NBSysResource> hasResources = userPermissionMapper.findResourcesByRoleId(roleId);
        List<LayuiXTree> treeList = new ArrayList<>(all.size());
        treeList.addAll(transTo(allGroup, NBSysResource::getGroup, NBSysResource::getGroup, hasResources::contains, res -> false));
        treeList.forEach(tree -> {
            List<NBSysResource> resources = resourceRepository.findAllByGroup(tree.getValue());
            tree.setData(transTo(resources, NBSysResource::getName, res -> res.getId().toString(), hasResources::contains, res -> false));
        });
        return treeList;
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
