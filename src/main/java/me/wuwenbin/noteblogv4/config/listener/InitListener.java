package me.wuwenbin.noteblogv4.config.listener;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.dao.repository.*;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBPanel;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysMenu;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRole;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRoleResource;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.RoleResourceKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Init.*;
import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Init.INIT_STATUS;
import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Param.*;

/**
 * spring boot 容器启动完成之后
 * 创建完表之后，插入一些初始值
 * created by Wuwenbin on 2018/7/15 at 17:14
 *
 * @author wuwenbin
 */
@Slf4j
@Component
@Order(1)
public class InitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ParamRepository paramRepository;
    private final RoleRepository roleRepository;
    private final PanelRepository panelRepository;
    private final NBContext context;
    private final RoleResourceRepository roleResourceRepository;
    private final ResourceRepository resourceRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public InitListener(ParamRepository paramRepository,
                        RoleRepository roleRepository,
                        PanelRepository panelRepository,
                        NBContext context,
                        RoleResourceRepository roleResourceRepository,
                        ResourceRepository resourceRepository, MenuRepository menuRepository) {
        this.paramRepository = paramRepository;
        this.roleRepository = roleRepository;
        this.panelRepository = panelRepository;
        this.context = context;
        this.roleResourceRepository = roleResourceRepository;
        this.resourceRepository = resourceRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("「笔记博客」App 正在准备，请稍后...");
        long roleCnt = roleRepository.count();
        //没有初始化角色信息
        if (roleCnt == 0) {
            log.info("「笔记博客」App 正在初始化权限系统，请稍后...");
            setUpAuthority(false);
            log.info("「笔记博客」App 权限系统初始化完毕...");
        } else {
            //已经包含初始化后的角色信息，查出角色名为ROLE_MASTER的对象，没有就抛出异常
            Optional<NBSysRole> role = roleRepository.findOne(Example.of(NBSysRole.builder().name("ROLE_MASTER").build()));
            context.setApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID,
                    role.orElseThrow(() -> new RuntimeException("未找到角色「ROLE_MASTER」")).getId());
            setUpAuthority(true);
        }

        setUpRootMenu();

        long panelCnt = panelRepository.count();
        if (panelCnt != NBPanel.PanelDom.values().length) {
            log.info("「笔记博客」App 正在初始化首页右侧面板设置，请稍后...");
            panelRepository.deleteAll();
            setUpPanel();
            log.info("「笔记博客」App 首页右侧面板初始化完毕...");
        }
        NBParam nbParam = paramRepository.findByName(INIT_STATUS);
        if (nbParam == null || StringUtils.isEmpty(nbParam.getValue()) || paramRepository.count() == 1) {
            setUpAppInitialSettings();
            setUpAppInitialText();
            log.info("「笔记博客」App 参数初始化完毕！");
        } else {
            log.info("「笔记博客」App 已经完成初始化，略过初始化步骤。");
        }
    }


    /**
     * 初始化网站角色信息以及把所有资源权限赋给网站管理员
     * 包含两中角色：管理员和访客
     * 访客需要去后台管理配置权限之后才能访问
     */
    private void setUpAuthority(boolean isRoleInitialed) {
        NBSysRole webmaster;
        //如果角色信息没有被初始化
        if (!isRoleInitialed) {
            //插入管理员角色信息
            NBSysRole webmasterRole = NBSysRole.builder().name("ROLE_MASTER").cnName("网站管理员").build();
            webmaster = roleRepository.saveAndFlush(webmasterRole);
            context.setApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID, webmaster.getId());
        } else {
            long webmasterRoleId = context.getApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID);
            webmaster = roleRepository.getOne(webmasterRoleId);
        }
        //获取扫描到的所有需要验证权限的资源
        List<Map<String, Object>> authResources = context.getApplicationObj(NoteBlogV4.Init.MASTER_RESOURCES);
        if (authResources != null) {
            authResources.forEach(res -> {
                String url = res.get("url").toString();
                String name = res.get("remark").toString();
                String permission = res.get("permission").toString();
                NBSysResource.ResType type = (NBSysResource.ResType) res.get("type");
                String group = res.get("group").toString();

                //数据库已存在此url，做更新操作
                if (resourceRepository.countByUrl(url) == 1) {
                    resourceRepository.updateByUrl(name, permission, type, group, url);
                }
                //数据库不存在，做插入操作
                else {
                    NBSysResource resourceInsert = NBSysResource.builder()
                            .permission(permission).name(name).url(url).type(type).group(group)
                            .build();
                    NBSysResource newRes = resourceRepository.saveAndFlush(resourceInsert);
                    NBSysRoleResource rr = NBSysRoleResource.builder()
                            .pk(new RoleResourceKey(webmaster.getId(), newRes.getId()))
                            .build();
                    roleResourceRepository.saveAndFlush(rr);
                }
            });
        }

        if (!isRoleInitialed) {
            //插入网站普通用户角色信息
            NBSysRole normalUser = NBSysRole.builder().name("ROLE_USER").cnName("网站访客").build();
            roleRepository.saveAndFlush(normalUser);
        }
    }

    /**
     * 插入根菜单至菜单表中
     */
    private void setUpRootMenu() {
        long cnt = menuRepository.countByParentId(0);
        if (cnt == 0) {
            log.info("「笔记博客」App 初始化设置根菜单...");
            NBSysMenu menu = NBSysMenu.builder()
                    .name("菜单根目录")
                    .icon("layui-icon layui-icon-home")
                    .type(NBSysMenu.MenuType.ROOT)
                    .parentId(0L).build();
            menuRepository.save(menu);
            log.info("「笔记博客」App 设置根菜单完毕");
        }
    }

    /**
     * 设置首页的一些文本变量
     */
    private void setUpAppInitialText() {
        String[][] words = new String[][]{
                {WEBSITE_TITLE, INIT_WEBSITE_TILE, "网站标题的文字", "10"},
                {FOOTER_WORDS, INIT_FOOTER_WORDS, "页脚的文字", "10"},
                {INDEX_TOP_WORDS, INIT_INDEX_TOP_WORDS, "首页置顶文字", "10"},
                {MENU_HOME, INIT_MENU_HOME, "导航菜单_首页", "10"},
                {MENU_PROJECT, INIT_MENU_PROJECT, "导航菜单_我的作品", "10"},
                {MENU_NOTE, INIT_MENU_NOTE, "导航菜单_笔记", "10"},
                {MENU_LINK, INIT_MENU_LINK, "导航菜单_额外的链接", "10"},
                {MENU_LINK_ICON, INIT_MENU_LINK_ICON, "导航菜单_额外的链接的字体图标logo", "10"},
                {MENU_LINK_HREF, StrUtil.EMPTY, "导航菜单_额外的链接url", "10"},
                {MENU_MINE, INIT_MENU_MINE, "导航菜单_关于我", "10"},
                {WECHAT_PAY, INIT_WECHAT_PAY, "微信付款码", "11"},
                {ALIPAY, INIT_ALIPAY, "支付宝付款码", "11"},
                {INFO_LABEL, INIT_INFO_LABEL, "信息板内容", "10"},
                {MENU_SEARCH, INIT_MENU_SEARCH, "导航菜单_搜索", "10"},
                {WEBSITE_LOGO_WORDS, INIT_WEBSITE_LOGO_WORDS, "网站logo的文字", "10"},
                {WEBSITE_LOGO_SMALL_WORDS, INIT_WEBSITE_LOGO_SMALL_WORDS, "网站logo的文字旁的小字", "10"},
                {COMMENT_NOTICE, INIT_COMMENT_WORD, "评论置顶公告", "10"},
                {MESSAGE_PANEL_WORDS, INIT_MESSAGE_PANEL_WORDS, "留言板的提示信息文字", "10"},
                {QINIU_DOMAIN, StrUtil.EMPTY, "七牛云文件服务器域名", "10"},
                {MAIL_SMPT_SERVER_ADDR, StrUtil.EMPTY, "SMTP服务器", "8"},
                {MAIL_SMPT_SERVER_PORT, StrUtil.EMPTY, "SMTP端口号", "8"},
                {MAIL_SERVER_ACCOUNT, StrUtil.EMPTY, "发件人邮箱", "8"},
                {MAIL_SENDER_NAME, StrUtil.EMPTY, "发件人昵称", "8"},
                {MAIL_SERVER_PASSWORD, StrUtil.EMPTY, "邮箱登入密码", "8"}
        };
        saveParam(words);
    }

    /**
     * 笔记博客的一些设置的初始值
     */
    private void setUpAppInitialSettings() {
        String[][] settings = new String[][]{
                {ALL_COMMENT_OPEN, INIT_SURE, "是否全局开放评论", "10"},
                {MENU_NOTE_SHOW, INIT_SURE, "导航菜单_笔记是否显示，默认显示", "10"},
                {MENU_PROJECT_SHOW, INIT_NOT, "导航菜单_我的作品是否显示，默认不显示", "10"},
                {MENU_MINE_SHOW, INIT_SURE, "导航菜单_关于我是否显示，默认显示", "10"},
                {MENU_SEARCH_SHOW, INIT_SURE, "导航菜单_搜索是否显示，默认显示", "10"},
                {MENU_LINK_SHOW, INIT_NOT, "是否显示额外的导航链接（譬如github）", "10"},
                {APP_ID, StrUtil.EMPTY, "qq登录API的app_id", "9"},
                {APP_KEY, StrUtil.EMPTY, "qq登录API的app_key", "9"},
                {QQ_LOGIN, INIT_NOT, "是否开放qq登录", "10"},
                {IS_SET_MASTER, INIT_NOT, "是否设置了网站管理员", "0"},
                {IS_OPEN_MESSAGE, INIT_NOT, "是否开启留言功能", "10"},
                {INFO_PANEL_ORDER, INIT_SURE, "网站信息和会员中心显示顺序，1表示网站信息显示在首要位置", "10"},
                {UPLOAD_TYPE, INIT_UPLOAD_TYPE, "上传方式类型，默认local，本地上传", "9"},
                {IS_OPEN_OSS_UPLOAD, INIT_NOT, "是否开启云服务器上传，默认0不开启", "9"},
                {QINIU_ACCESS_KEY, StrUtil.EMPTY, "七牛云AccessKey", "9"},
                {QINIU_SECRET_KEY, StrUtil.EMPTY, "七牛云SecretKey", "9"},
                {QINIU_BUCKET, StrUtil.EMPTY, "七牛云bucket", "9"},
                {PAGE_MODERN, INIT_DEFAULT_PAGE_MODERN, "首页博文分页模式0：流式，1：按钮加载", "10"},
                {BLOG_STYLE, NoteBlogV4.ParamValue.STYLE_NORMAL, "首页样式，简约/普通（simple/normal）", "10"},
                {BLOG_INDEX_PAGE_SIZE, INIT_DEFAULT_PAGE_SIZE, "博客首页文章页面数据量大小,-1则根据参数来判断", "-1"},
                {STATISTIC_ANALYSIS, INIT_NOT, "是否开启访问统计，默认不开启", "10"},
                {ARTICLE_SUMMARY_WORDS_LENGTH, "243", "首页展示文章的摘要的文字数量，默认243", "10"}
        };
        saveParam(settings);
    }

    /**
     * 设置初始化面板
     */
    private void setUpPanel() {
        for (NBPanel.PanelDom panelDom : NBPanel.PanelDom.values()) {
            NBPanel panel = NBPanel.builder()
                    .panelDom(panelDom)
                    .orderIndex(panelDom.getOrder())
                    .titleName(panelDom.getTitleName())
                    .build();
            panelRepository.saveAndFlush(panel);
        }
    }

    /**
     * 保存一条参数记录
     *
     * @param params
     */
    private void saveParam(String[][] params) {
        Arrays.stream(params).forEach(
                hpw -> {
                    NBParam item = NBParam.builder()
                            .name(hpw[0])
                            .value(hpw[1])
                            .remark(hpw[2])
                            .level(Integer.valueOf(hpw[3]))
                            .build();
                    //保存每个设置的初始值
                    paramRepository.saveAndFlush(item);
                }
        );
    }

}
