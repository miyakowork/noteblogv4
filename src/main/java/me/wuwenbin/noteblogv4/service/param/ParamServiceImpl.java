package me.wuwenbin.noteblogv4.service.param;

import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

}
