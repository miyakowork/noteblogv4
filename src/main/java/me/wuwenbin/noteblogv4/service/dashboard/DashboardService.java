package me.wuwenbin.noteblogv4.service.dashboard;

import me.wuwenbin.noteblogv4.model.pojo.vo.BaseDataStatistics;
import me.wuwenbin.noteblogv4.model.pojo.vo.LatestComment;

/**
 * created by Wuwenbin on 2019/1/7 at 14:03
 */
public interface DashboardService {

    /**
     * 首页统计数据面板
     *
     * @return
     */
    BaseDataStatistics calculateData();


    /**
     * 找出最新的一条评论
     *
     * @return
     */
    LatestComment findLatestComment();
}
