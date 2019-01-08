package me.wuwenbin.noteblogv4.model.pojo.vo;

import lombok.Builder;
import lombok.Data;
import me.wuwenbin.noteblogv4.model.entity.NBLogger;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * created by Wuwenbin on 2019-01-08 at 18:58
 */
@Data
@Builder
public class Statistics implements Serializable {

    /**
     * 5个最近日期
     */
    private List<LocalDate> dates;
    private List<NBLogger> loggerItems;
}
