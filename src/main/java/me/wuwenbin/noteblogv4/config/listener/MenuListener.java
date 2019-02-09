package me.wuwenbin.noteblogv4.config.listener;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.MenuRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.dao.repository.ResourceRepository;
import me.wuwenbin.noteblogv4.dao.repository.RoleRepository;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu.MenuType;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Init.INIT_NOT;
import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Init.INIT_STATUS;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu.MenuType.LEAF;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu.MenuType.PARENT;

/**
 * created by Wuwenbin on 2018/8/1 at 20:25
 *
 * @author wuwenbin
 */
@Component
@Order(2)
@Slf4j
public class MenuListener implements ApplicationListener<ApplicationReadyEvent> {

    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
    private final ParamRepository paramRepository;
    private final Environment environment;

    @Autowired
    public MenuListener(MenuRepository menuRepository, RoleRepository roleRepository,
                        ParamRepository paramRepository, ResourceRepository resourceRepository,
                        Environment environment) {
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
        this.paramRepository = paramRepository;
        this.resourceRepository = resourceRepository;
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        NBParam nbParam = paramRepository.findByName(INIT_STATUS);
        long cnt = menuRepository.count();
        long rootCnt = menuRepository.countByType(MenuType.ROOT.name());
        if (nbParam == null || StringUtils.isEmpty(nbParam.getValue())) {
            throw new RuntimeException("初始化参数有误，未找到 init_status 参数！");
        } else if (nbParam.getValue().equals(INIT_NOT) && cnt == 1 && rootCnt == 1) {
            log.info("笔记博客」App 正在初始化菜单，请稍后...");
            Object[][] folderMenus = new Object[][]{
                    {"权限管理", "layui-icon layui-icon-auz", new String[][]{
                            {"/management/menu", "菜单管理", "fa fa-list-ul"}
                            , {"/management/role", "角色管理", "fa fa-user-o"}
                            , {"/management/users", "用户管理", "fa fa-users"}
                    }},
                    {"内容发布", "layui-icon layui-icon-edit", new String[][]{
                            {"/management/article/post", "发布文章", "fa fa-send-o"}
                            , {"/management/note/post", "随记随笔", "fa fa-file-text-o"}
                    }},
                    {"内容管理", "layui-icon layui-icon-template-1", new String[][]{
                            {"/management/article", "文章管理", "fa fa-newspaper-o"}
                            , {"/management/note", "随笔管理", "fa fa-file-o"}
                    }},
                    {"字典管理", "layui-icon layui-icon-read", new String[][]{
                            {"/management/dictionary/cate", "分类管理", "fa fa-clone"}
                            , {"/management/dictionary/projectCate", "项目分类管理", "fa fa-hdd-o"}
                            , {"/management/dictionary/keyword", "关键字管理", "fa fa-dot-circle-o"}
                            , {"/management/dictionary/tag", "标签管理", "fa fa-tags"}
                    }},
                    {"偏好设置", "layui-icon layui-icon-set", new String[][]{
                            {"/management/settings/qrcode", "二维码设置", "fa fa-qrcode"}
                            , {"/management/settings/common", "网站基本设置", "fa fa-cogs"}
                            , {"/management/settings/theme", "网站风格设置", "fa fa-cog"}
                            , {"/management/settings/profile", "个人资料", "fa fa-address-card-o"}
                            , {"/management/settings/mail", "邮件服务器", "fa fa-server"}
                    }},
                    {"个人内容", "layui-icon layui-icon-diamond", new String[][]{
                            {"/management/profile", "关于内容", "fa fa-hdd-o"}
                            , {"/management/project", "资源项目分享", "fa fa-laptop"}
                    }}
                    ,
                    {"消息管理", "layui-icon layui-icon-username", new String[][]{
                            {"/management/comment", "评论管理", "fa fa-comments-o"}
                            , {"/management/message", "留言管理", "fa fa-globe"}
                    }}

            };

            setUpMenuSystem(folderMenus);

        }
        hideAuthMenu();
        log.info("「笔记博客」App 初始化菜单完毕");
    }

    /**
     * 查找root 菜单的 id
     *
     * @return
     */
    private long findRootId() {
        return menuRepository.findByType(MenuType.ROOT).getId();
    }

    /**
     * 拼接菜单
     *
     * @param icon
     * @param name
     * @param orderIndex
     * @param parentId
     * @param roleId
     * @param type
     * @return
     */
    private NBSysMenu fixMenu(String icon, String name, int orderIndex,
                              long parentId, long roleId, MenuType type) {
        return NBSysMenu.builder()
                .icon(icon)
                .name(name)
                .parentId(parentId)
                .type(type)
                .enable(true)
                .orderIndex(orderIndex)
                .roleId(roleId)
                .build();

    }

    /**
     * 初始化菜单权限
     */
    private void setUpMenuSystem(Object[][] folderMenus) {
        Optional<NBSysRole> role = roleRepository.findOne(Example.of(NBSysRole.builder().name("ROLE_MASTER").build()));
        long roleId = role.orElseThrow(() -> new RuntimeException("未找到角色「ROLE_MASTER」")).getId();
        long rootId = findRootId();

        saveTopMenu("layui-icon layui-icon-console", "仪表盘", "/management/dashboard", rootId, roleId);

        for (int i = 0; i < folderMenus.length; i++) {
            Object[] parent = folderMenus[i];
            NBSysMenu parentMenu = fixMenu(parent[1].toString(), parent[0].toString(), i + 1, rootId, roleId, PARENT);
            NBSysMenu genMenuLoop = menuRepository.save(parentMenu);
            long genMenuId = genMenuLoop.getId();
            String[][] leafMenus = (String[][]) parent[2];
            for (int j = 0; j < leafMenus.length; j++) {
                String[] leafMenu = leafMenus[j];
                String url = leafMenu[0];
                NBSysMenu lm = fixMenu(leafMenu[2], leafMenu[1], j, genMenuId, roleId, LEAF);
                NBSysMenu genLeafMenu = menuRepository.save(lm);
                NBSysResource res = resourceRepository.findByUrl(url);
                menuRepository.updateResourceById(genLeafMenu.getId(), res.getName(), res);
            }
        }

    }

    /**
     * 更新权限菜单状态
     */
    private void hideAuthMenu() {
        String[] menus = new String[]{
                "/management/menu",
                "/management/role",
                "/management/users"
        };
        boolean enable = environment.getProperty("noteblog.menu.auth", Boolean.class, false);
        for (String menu : menus) {
            NBSysResource r = resourceRepository.findByUrl(menu);
            NBSysMenu m = menuRepository.findByResourceId(r.getId());
            menuRepository.updateEnableById(enable, m.getId());
        }
        menuRepository.updateAuthParentMenu(enable);
    }


    /**
     * 新增一个顶级菜单
     *
     * @param icon
     * @param name
     * @param url
     * @param rootId
     * @param roleId
     */
    private void saveTopMenu(String icon, String name, String url, long rootId, long roleId) {
        NBSysMenu menu = fixMenu(icon, name, 0, rootId, roleId, PARENT);
        NBSysMenu genMenu = menuRepository.save(menu);
        NBSysResource genMenuRes = resourceRepository.findByUrl(url);
        menuRepository.updateResourceById(genMenu.getId(), genMenuRes.getName(), genMenuRes);
    }
}
