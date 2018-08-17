package me.wuwenbin.noteblogv4.dao.mapper;

import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import me.wuwenbin.noteblogv4.model.pojo.vo.NBTagVO;

import java.util.List;

/**
 * created by Wuwenbin on 2018/8/4 at 11:20
 *
 * @author wuwenbin
 */
@MybatisDao
public interface TagMapper {

    /**
     * 根据文章id查找此篇文章对应的所有tag
     *
     * @param articleId
     * @return
     */
    List<NBTagVO> findTagNamesByArticleId(Long articleId);
}
