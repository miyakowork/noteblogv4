package me.wuwenbin.noteblogv4.dao.mapper;

import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import me.wuwenbin.noteblogv4.model.entity.NBTag;

import java.util.List;

/**
 * created by Wuwenbin on 2018/8/4 at 11:20
 * @author wuwenbin
 */
@MybatisDao
public interface TagMapper {

    /**
     * 查找所有tag
     * @return
     */
    List<NBTag> findAll();

}
