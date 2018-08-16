package me.wuwenbin.noteblogv4.service.param;

import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Map;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Param.STATISTIC_ANALYSIS;

/**
 * created by Wuwenbin on 2018/7/17 at 15:52
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ParamServiceImpl implements ParamService {

    private final ParamRepository paramRepository;

    @Autowired
    public ParamServiceImpl(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    @Override
    public <T> T getValueByName(String name) {
        NBParam param = paramRepository.findByName(name);
        return param != null ? param.getValue() : null;
    }

    @Override
    public void saveInitParam(Map<String, String[]> map) {
        Map<String, Object> param = NBUtils.getParameterMap(map);
        for (Map.Entry<String, Object> next : param.entrySet()) {
            String key = next.getKey();
            if (!"username".equals(key) && !"password".equals(key)) {
                if (!StringUtils.isEmpty(next.getValue())) {
                    String value = (String) next.getValue();
                    paramRepository.updateInitParam(value, key);
                    if ("upload_type".equals(key) && !"LOCAL".equals(next.getValue())) {
                        paramRepository.updateInitParam("1", "is_open_oss_upload");
                    }
                }
            }
        }
    }

    @Override
    @Cacheable(value = "paramCache", key = "'statistic_analysis'")
    public boolean isOpenStatisticAnalysis() {
        NBParam p = paramRepository.findByName(STATISTIC_ANALYSIS);
        final String open = "1";
        if (p != null) {
            return open.equals(p.getValue());
        }
        return false;
    }
}
