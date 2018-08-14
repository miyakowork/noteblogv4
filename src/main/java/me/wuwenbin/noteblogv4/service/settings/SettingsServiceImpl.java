package me.wuwenbin.noteblogv4.service.settings;

import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.constant.Upload;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        return update(name, value, NBR::ok);
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
                String value3 = val.split(comma)[2];
                paramRepository.updateValueByName("menu_link", value2);
                paramRepository.updateValueByName("menu_link_href", value3);
            }
            return NBR.ok("更新成功！");
        });
    }
}
