package me.wuwenbin.noteblogv4.model.pojo.business;

import lombok.Data;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Wuwenbin on 2018/7/31 at 15:05
 * @author wuwenbin
 */
@Data
public class MenuTree implements Serializable {

    private Long id;
    private Long parentId;
    private String url;
    private String name;
    private String icon;
    private List<MenuTree> data;

    public MenuTree(Long id, Long parentId, String url, String name, String icon) {
        this.id = id;
        this.parentId = parentId;
        this.url = url;
        this.name = name;
        this.icon = icon;
    }

    private static MenuTree findChildren(MenuTree dataNode, List<MenuTree> dataNodes) {
        for (MenuTree it : dataNodes) {
            if (dataNode.getId().equals(it.getParentId())) {
                if (dataNode.getData() == null) {
                    dataNode.setData(new ArrayList<>());
                }
                dataNode.getData().add(findChildren(it, dataNodes));
            }
        }
        return dataNode;
    }

    /**
     * 使用递归方法建树
     *
     * @param dataNodes
     * @return
     */
    public static List<MenuTree> buildByRecursive(List<NBSysMenu> dataNodes) {
        List<MenuTree> trees = new ArrayList<>(dataNodes.size());
        dataNodes.forEach(dataNode -> {
            MenuTree menuTree = new MenuTree(dataNode.getId(),
                    dataNode.getParentId(),
                    dataNode.getResource() != null ? dataNode.getResource().getUrl() : null,
                    dataNode.getName(), dataNode.getIcon());
            trees.add(menuTree);
        });
        List<MenuTree> data = new ArrayList<>(20);
        for (MenuTree dataNode : trees) {
            if (0 == dataNode.getParentId()) {
                data.add(findChildren(dataNode, trees));
            }
        }
        return data;
    }
}
