package me.wuwenbin.noteblogv4.web.management.settings;

import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.service.settings.SettingsService;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.AJAX;
import static me.wuwenbin.noteblogv4.config.permission.NBAuth.Group.ROUTER;
import static me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource.ResType.NAV_LINK;

/**
 * created by Wuwenbin on 2018/8/11 at 16:14
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class SettingsController extends BaseController {

    private final ParamRepository paramRepository;
    private final SettingsService settingsService;

    @Autowired
    public SettingsController(ParamRepository paramRepository, SettingsService settingsService) {
        this.paramRepository = paramRepository;
        this.settingsService = settingsService;
    }

    @RequestMapping("/settings/common")
    @NBAuth(value = "management:settings:common", remark = "网站基本设置界面", group = ROUTER, type = NAV_LINK)
    public String settingsCommon(Model model) {
        List<NBParam> params = paramRepository.findAllByLevelGreaterThanEqual(9);
        Map<String, Object> attributeMap = params.stream().collect(Collectors.toMap(NBParam::getName, NBParam::getValue));
        model.addAllAttributes(attributeMap);
        return "/management/settings/common";
    }

    @RequestMapping("/settings/mail")
    @NBAuth(value = "management:settings:mail", remark = "网站发送邮件服务器设置", group = ROUTER, type = NAV_LINK)
    public String settingsMail(Model model) {
        List<NBParam> params = paramRepository.findAllByLevel(8);
        Map<String, Object> attributeMap = params.stream().collect(Collectors.toMap(NBParam::getName, NBParam::getValue));
        model.addAllAttributes(attributeMap);
        return "/management/settings/mail";
    }

    @RequestMapping("/settings/update")
    @NBAuth(value = "management:settings:update", remark = "网站设置修改操作", group = AJAX)
    @ResponseBody
    public NBR update(String type, String name, String value) {
        final String switches = "switch", text = "text";
        if (StringUtils.isEmpty(type)) {
            return NBR.error("修改参数类型为空！");
        }
        if (switches.equalsIgnoreCase(type)) {
            return settingsService.updateSwitch(name, value);
        } else if (text.equalsIgnoreCase(type)) {
            return settingsService.updateText(name, value);
        } else {
            return NBR.error("未识别修改参数类型！");
        }
    }
}
