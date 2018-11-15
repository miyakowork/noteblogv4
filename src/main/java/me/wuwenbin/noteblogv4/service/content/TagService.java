package me.wuwenbin.noteblogv4.service.content;

import me.wuwenbin.noteblogv4.model.constant.TagType;
import me.wuwenbin.noteblogv4.model.pojo.vo.NBTagVO;

import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2018/8/20 at 11:24
 *
 * @author wuwenbin
 */
public interface TagService {

    /**
     * 查找文章/笔记相关tag并selected
     *
     * @param referId
     * @param type    文章还是笔记{@code TagType}
     * @return
     */
    List<NBTagVO> findSelectedTagsByReferId(Long referId, TagType type);

    /**
     * 查询标签使用数到首页标签面板上显示
     *
     * @return
     */
    List<Map<String, Object>> findTagsTab();
}
