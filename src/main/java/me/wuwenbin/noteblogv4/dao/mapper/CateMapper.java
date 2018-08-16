package me.wuwenbin.noteblogv4.dao.mapper;

import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import org.apache.ibatis.annotations.Param;

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
    Page<NBCate> findPageInfo(Pagination<NBCate> pagination, @Param("cateQueryBO") NBCate cateQueryBO);

    /**
     * 查找已存在对应的分类数目
     *
     * @param name
     * @param cnName
     * @return
     */
    long findCateCount(@Param("name") String name, @Param("cnName") String cnName);
}
