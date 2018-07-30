package me.wuwenbin.noteblogv4.web.management;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group;

/**
 * created by Wuwenbin on 2018/7/21 at 23:31
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class AdminIndexController {

    @RequestMapping("/index")
    @NBAuth(value = "management:index:page", remark = "后台管理首页", type = ResType.NAV_LINK, group = Group.PAGE)
    public String index() {
        return "admin_index";
    }

    @RequestMapping("/dashboard")
    @NBAuth(value = "management:index:dashboard", remark = "管理页面仪表盘界面", group = Group.ROUTER)
    public String dashboard() {
        return "management/dashboard";
    }
}

