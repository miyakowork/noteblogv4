package me.wuwenbin.noteblogv4.service.dictionary;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import me.wuwenbin.noteblogv4.dao.mapper.CateMapper;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2018/8/15 at 17:00
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class CateServiceImpl implements CateService {

    private final CateMapper cateMapper;

    @Autowired
    public CateServiceImpl(CateMapper cateMapper) {
        this.cateMapper = cateMapper;
    }

    @Override
    public Page<NBCate> findPageInfo(Pagination<NBCate> catePage, NBCate cateQueryBO) {
        PageHelper.startPage(catePage.getPage(), catePage.getLimit(), catePage.getOrderBy());
        return cateMapper.findPageInfo(catePage, cateQueryBO);
    }
}
