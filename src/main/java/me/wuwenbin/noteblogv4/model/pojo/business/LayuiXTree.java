package me.wuwenbin.noteblogv4.model.pojo.business;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * layuiXtree树对象
 * created by Wuwenbin on 2018/7/23 at 16:55
 *
 * @author wuwenbin
 */
@Data
public class LayuiXTree implements Serializable {

    private String id;
    private String title;
    private String value;
    private String parentId;
    private Boolean checked;
    private Boolean disabled;
    private List<LayuiXTree> data = new ArrayList<>();

    public LayuiXTree(String title, String value, String id, boolean checked, boolean disabled) {
        this.title = title;
        this.value = value;
        this.id = id;
        this.checked = checked;
        this.disabled = disabled;
        this.parentId = getPid(id);
    }

    private LayuiXTree(String id, String parentId) {
        this.title = id;
        this.value = "";
        this.id = id;
        this.checked = false;
        this.disabled = false;
        this.parentId = parentId;
    }


    private static String getPid(String gp) {
        if (StringUtils.isEmpty(gp)) {
            throw new IllegalArgumentException("title 字段不能为空！");
        } else {
            String[] groupArray = gp.split(":");
            int length = groupArray.length;
            if (length == 1) {
                return "root";
            } else {
                return gp.substring(0, gp.lastIndexOf(":"));
            }
        }
    }

    private static LayuiXTree findChildren(LayuiXTree treeNode, List<LayuiXTree> treeNodes) {
        for (LayuiXTree it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getData() == null) {
                    treeNode.setData(new ArrayList<>());
                }
                treeNode.getData().add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static List<LayuiXTree> buildByRecursive(List<LayuiXTree> treeNodes) {
        List<LayuiXTree> trees = new ArrayList<>(treeNodes);
        //权限标识是三级的，故此循环两次即可，此处后续可改为动态循环次数
        List<LayuiXTree> newTrees = iteratorIt(iteratorIt(trees));
        List<LayuiXTree> data = new ArrayList<>(20);
        for (LayuiXTree treeNode : newTrees) {
            if ("root".equals(treeNode.getParentId())) {
                data.add(findChildren(treeNode, newTrees));
            }
        }
        return data;
    }

    private static List<LayuiXTree> iteratorIt(List<LayuiXTree> data) {
        List<LayuiXTree> trees = new ArrayList<>(data);
        data.forEach(d -> {
            LayuiXTree xTree = new LayuiXTree(d.getParentId(), getPid(d.getParentId()));
            long cnt1 = trees.stream().filter(tree -> tree.getId().equalsIgnoreCase(d.getParentId())).count();
            if (cnt1 == 0) {
                trees.add(xTree);
            }
        });
        return trees;
    }
}
