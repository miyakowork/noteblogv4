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

    private String text;
    private long sum;
    @Builder.Default
    private String url = "";
}
