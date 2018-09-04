package me.wuwenbin.noteblogv4.dao.mapper;

import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;
import me.wuwenbin.noteblogv4.model.pojo.vo.NBArticleVO;
import org.apache.ibatis.annotations.Param;

/**
 * created by Wuwenbin on 2018/8/9 at 15:24
 *
 * @author wuwenbin
 */
@MybatisDao
public interface ArticleMapper {

    /**
     * 查找分页信息
     *
     * @param pagination
     * @param title
     * @param authorId
     * @return
     */
    Page<NBArticleVO> findPageInfo(Pagination<NBArticleVO> pagination, @Param("title") String title, @Param("authorId") Long authorId);

}
