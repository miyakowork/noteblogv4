package me.wuwenbin.noteblogv4.config.listener;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.PanelRepository;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.dao.repository.RoleRepository;
import me.wuwenbin.noteblogv4.model.entity.NBPanel;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

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
public class NoteBlogInitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ParamRepository paramRepository;
    private final RoleRepository roleRepository;
    private final PanelRepository panelRepository;

    @Autowired
    public NoteBlogInitListener(ParamRepository paramRepository, RoleRepository roleRepository, PanelRepository panelRepository) {
        this.paramRepository = paramRepository;
        this.roleRepository = roleRepository;
        this.panelRepository = panelRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("「笔记博客」App 正在准备，请稍后...");
        NBParam nbParam = paramRepository.findByNameEquals(INIT_STATUS);
        long roleCnt = roleRepository.count();
        if (roleCnt == 0) {
            setUpRoles();
        }
        long panelCnt = panelRepository.count();
        if (panelCnt != NBPanel.PanelDom.values().length) {
            panelRepository.deleteAll();
            setUpPanel();
        }
        if (nbParam == null || StringUtils.isEmpty(nbParam.getValue())) {
            log.info("「笔记博客」App 正在初始化中，请稍后...");
            setUpAppInitialState();
            setUpAppInitialSettings();
            setUpAppInitialText();
            log.info("「笔记博客」App 初始化完毕！");
        } else {
            log.info("「笔记博客」App 已经初始化，略过初始化步骤。");
        }
        log.info("「笔记博客」App 启动完毕。");

    }

    /**
     * 在参数表中插入一条记录
     * 记录程序是否被初始化过（有没有在初始化界面设置过东西，设置过改为1）
     * 当然，此处是程序第一次启动，当然插入值是未初始化过的：0
     */
    private void setUpAppInitialState() {
        NBParam initStatus = NBParam.builder()
                .name(INIT_STATUS)
                .value(INIT_NOT)
                .remark("标记用户是否在「笔记博客」App 的初始化设置页面设置过")
                .build();
        paramRepository.save(initStatus);
    }

    /**
     * 初始化网站角色信息
     * 包含两中角色：管理员和访客
     */
    private void setUpRoles() {
        String[][] roles = new String[][]{
                {"ROLE_MASTER", "网站管理员"},
                {"ROLE_USER", "网站访客"}
        };
        Arrays.stream(roles).forEach(role -> {
            NBSysRole r = NBSysRole.builder().name(role[0]).cnName(role[1]).build();
            roleRepository.saveAndFlush(r);
        });
    }

    /**
     * 设置首页的一些文本变量
     */
    private void setUpAppInitialText() {
        String[][] words = new String[][]{
                {WEBSITE_TITLE, INIT_WEBSITE_TILE, "网站标题的文字"},
                {FOOTER_WORDS, INIT_FOOTER_WORDS, "页脚的文字"},
                {INDEX_TOP_WORDS, INIT_INDEX_TOP_WORDS, "首页置顶文字"},
                {MENU_HOME, INIT_MENU_HOME, "导航菜单_首页"},
                {MENU_NOTE, INIT_MENU_NOTE, "导航菜单_笔记"},
                {MENU_LINK, INIT_MENU_LINK, "导航菜单_额外的链接"},
                {MENU_LINK_ICON, INIT_MENU_LINK_ICON, "导航菜单_额外的链接的字体图标logo"},
                {MENU_LINK_HREF, null, "导航菜单_额外的链接url"},
                {MENU_MINE, INIT_MENU_MINE, "导航菜单_关于我"},
                {WECHAT_PAY, INIT_WECHAT_PAY, "微信付款码"},
                {ALIPAY, INIT_ALIPAY, "支付宝付款码"},
                {INFO_LABEL, INIT_INFO_LABEL, "信息板内容"},
                {MENU_SEARCH, INIT_MENU_SEARCH, "导航菜单_搜索"},
                {WEBSITE_LOGO_WORDS, INIT_WEBSITE_LOGO_WORDS, "网站logo的文字"},
                {COMMENT_NOTICE, INIT_COMMENT_WORD, "评论置顶公告"},
                {MESSAGE_PANEL_WORDS, INIT_MESSAGE_PANEL_WORDS, "留言板的提示信息文字"}
        };
        saveParam(words);
    }

    /**
     * 笔记博客的一些设置的初始值
     */
    private void setUpAppInitialSettings() {
        String[][] settings = new String[][]{
                {ALL_COMMENT_OPEN, INIT_SURE, "是否全局开放评论"},
                {MENU_LINK_SHOW, INIT_NOT, "是否显示额外的导航链接（譬如github）"},
                {APP_ID, null, "qq登录API的app_id"},
                {APP_KEY, null, "qq登录API的app_key"},
                {QQ_LOGIN, INIT_NOT, "是否开放qq登录"},
                {IS_SET_MASTER, INIT_NOT, "是否设置了网站管理员"},
                {IS_OPEN_MESSAGE, INIT_NOT, "是否开启留言功能"},
                {IS_OPEN_MESSAGE, INIT_NOT, "是否开启留言功能"},
                {INFO_PANEL_ORDER, INIT_SURE, "网站信息和会员中心显示顺序，1表示网站信息显示在首要位置"},
                {UPLOAD_TYPE, INIT_UPLOAD_TYPE, "上传方式类型，默认local，本地上传"},
                {IS_OPEN_OSS_UPLOAD, INIT_NOT, "是否开启云服务器上传，默认0不开启"},
                {QINIU_ACCESS_KEY, null, "七牛云AccessKey"},
                {QINIU_SECRET_KEY, null, "七牛云SecretKey"},
                {QINIU_BUCKET, null, "七牛云bucket"},
                {PAGE_MODERN, INIT_DEFAULT_PAGE_MODERN, "首页博文分页模式0：流式，1：按钮加载"},
                {BLOG_INDEX_PAGE_SIZE, INIT_DEFAULT_PAGE_SIZE, "首页博文分页模式0：流式，1：按钮加载"}
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
                            .remark(hpw[2]).build();
                    //保存每个设置的初始值
                    paramRepository.saveAndFlush(item);
                }
        );
    }

}
