package me.wuwenbin.noteblogv4.web.management;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/7/21 at 23:31
 */
@Controller
@RequestMapping("/management")
public class AdminIndexController {

    @NBAuth(value = "management:index", remark = "后台管理首页", type = ResType.NAV_LINK, group = "dashboard")
    @RequestMapping("/index")
    public String index() {
        return "admin_index";
    }
}

