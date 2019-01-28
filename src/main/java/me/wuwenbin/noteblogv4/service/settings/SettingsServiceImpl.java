package me.wuwenbin.noteblogv4.service.settings;

import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.constant.Upload;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.util.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Param.*;

/**
 * created by Wuwenbin on 2018/8/13 at 15:09
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class SettingsServiceImpl implements SettingsService {

    private final ParamRepository paramRepository;

    @Autowired
    public SettingsServiceImpl(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    @Override
    public NBR updateSwitch(String name, String value) {
        if (name.equalsIgnoreCase(NoteBlogV4.Param.IS_OPEN_OSS_UPLOAD)) {
            CacheUtils.removeParamCache(name);
            CacheUtils.removeParamCache(NoteBlogV4.Param.UPLOAD_TYPE);
            final String type = "0".equalsIgnoreCase(value) ? Upload.Method.LOCAL.name() : Upload.Method.QINIU.name();
            paramRepository.updateValueByName(NoteBlogV4.Param.UPLOAD_TYPE, type);
        }
        if (NoteBlogV4.Param.STATISTIC_ANALYSIS.equalsIgnoreCase(name)) {
            return update(name, value, () -> {
                CacheUtils.removeParamCache(name);
                return NBR.ok();
            });
        } else {
            return update(name, value, NBR::ok);
        }
    }

    @Override
    public NBR updateText(String name, String value) {
        final String menuLink = "menu_link_icon";
        final String comma = ",";
        final String val = value;
        if (val.split(comma).length > 0) {
            value = val.split(comma)[0];
        }
        return update(name, value, () -> {
            if (name.equalsIgnoreCase(NoteBlogV4.Param.IS_OPEN_OSS_UPLOAD)) {
                final String type = "0".equalsIgnoreCase(val) ? Upload.Method.LOCAL.name() : Upload.Method.QINIU.name();
                paramRepository.updateValueByName(NoteBlogV4.Param.UPLOAD_TYPE, type);
            } else if (menuLink.equalsIgnoreCase(name) && val.split(comma).length > 0) {
                String value2 = val.split(comma)[1];
                String value3 = "";
                if (val.split(comma).length == 3) {
                    value3 = val.split(comma)[2];
                }
                paramRepository.updateValueByName("menu_link", value2);
                paramRepository.updateValueByName("menu_link_href", value3);
            }
            return NBR.ok("更新成功！");
        });
    }

    @Override
    public NBR updateMailConfig(Map<String, Object> paramMap) {
        paramRepository.updateValueByName(MAIL_SMPT_SERVER_ADDR, paramMap.get(MAIL_SMPT_SERVER_ADDR).toString());
        paramRepository.updateValueByName(MAIL_SMPT_SERVER_PORT, paramMap.get(MAIL_SMPT_SERVER_PORT).toString());
        paramRepository.updateValueByName(MAIL_SERVER_ACCOUNT, paramMap.get(MAIL_SERVER_ACCOUNT).toString());
        paramRepository.updateValueByName(MAIL_SERVER_PASSWORD, paramMap.get(MAIL_SERVER_PASSWORD).toString());
        paramRepository.updateValueByName(MAIL_SENDER_NAME, paramMap.get(MAIL_SENDER_NAME).toString());
        return NBR.ok("更新邮件服务器配置成功！");
    }
}
