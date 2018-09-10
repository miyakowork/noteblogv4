package me.wuwenbin.noteblogv4.model.pojo.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2018/9/7 at 11:05
 *
 * @author wuwenbin
 */
@ToString
@Data
public class CommentQueryBO implements Serializable {
    private Long articleId;
    private String clearComment;
    private String ipCnAddr;
    private Long userId;
}
