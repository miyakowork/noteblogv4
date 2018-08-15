package me.wuwenbin.noteblogv4.dao.mapper;

import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;

/**
 * created by Wuwenbin on 2018/8/15 at 16:35
 *
 * @author wuwenbin
 */
@MybatisDao
public interface CateMapper {

    /**
     * 分页信息
     *
     * @param pagination
     * @param cateQueryBO
     * @return
     */
    Page<NBCate> findPageInfo(Pagination<NBCate> pagination, NBCate cateQueryBO);
}
