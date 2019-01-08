package me.wuwenbin.noteblogv4.model.pojo.vo;

import lombok.Builder;
import lombok.Data;
import me.wuwenbin.noteblogv4.model.entity.NBComment;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2019/1/7 at 14:11
 *
 * @author wuwenbin
 */
@Data
@Builder
public class LatestComment implements Serializable {

    private Long articleId;
    private String articleTitle;
    private LocalDateTime articleDate;
    private NBComment comment;
}
