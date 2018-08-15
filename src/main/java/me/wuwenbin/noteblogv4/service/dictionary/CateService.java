package me.wuwenbin.noteblogv4.service.dictionary;

import com.github.pagehelper.Page;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
import me.wuwenbin.noteblogv4.model.pojo.framework.Pagination;

/**
 * created by Wuwenbin on 2018/8/15 at 16:55
 *
 * @author wuwenbin
 */
public interface CateService {

    /**
     * 查找分类分页信息
     *
     * @param catePage
     * @param cateQueryBO
     * @return
     */
    Page<NBCate> findPageInfo(Pagination<NBCate> catePage, NBCate cateQueryBO);

    /**
     * 查找数据库中是否已存在相关的cate
     *
     * @param cate
     * @return
     */
    boolean findIfExist(NBCate cate);
}
