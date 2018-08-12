package me.wuwenbin.noteblogv4.model.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;

/**
 * created by Wuwenbin on 2018/8/9 at 15:18
 *
 * @author wuwenbin
 */
@Getter
@Setter
@ToString
public class NBArticleVO extends NBArticle {
    private String cnName;
}
