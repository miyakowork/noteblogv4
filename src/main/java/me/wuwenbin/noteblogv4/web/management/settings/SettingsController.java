package me.wuwenbin.noteblogv4.web.management.settings;

import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/8/11 at 16:14
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class SettingsController extends BaseController {

    @RequestMapping("/settings")
    public String settingsIndex(Model model) {
        return "management/settings/settings";
    }
}
