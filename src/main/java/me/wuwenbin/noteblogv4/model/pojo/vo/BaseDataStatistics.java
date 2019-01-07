package me.wuwenbin.noteblogv4.model.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/1/7 at 13:59
 *
 * @author wuwenbin
 */
@Data
@Builder
public class BaseDataStatistics implements Serializable {

    private Integer articles;
    private Integer notes;
    private Integer messages;
    private Integer users;
    private Integer todayComments;
    private Integer todayNewUsers;
    private Integer todayNewComments;
    private Integer allSumIps;
    private Integer todaySumIps;
}
