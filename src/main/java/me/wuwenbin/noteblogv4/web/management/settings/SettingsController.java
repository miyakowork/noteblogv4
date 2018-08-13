package me.wuwenbin.noteblogv4.web.management.settings;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;

/**
 * created by Wuwenbin on 2018/8/11 at 16:14
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class SettingsController extends BaseController {

    @RequestMapping("/settings")
    @NBAuth(value = "management:settings:page", remark = "网站基本设置界面", group = ROUTER)
    public String settingsIndex(Model model) {
        return "management/settings/settings";
    }
}
