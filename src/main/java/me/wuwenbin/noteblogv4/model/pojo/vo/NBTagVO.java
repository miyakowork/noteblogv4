package me.wuwenbin.noteblogv4.model.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.wuwenbin.noteblogv4.model.entity.NBTag;

/**
 * created by Wuwenbin on 2018/8/10 at 10:31
 * @author wuwenbin
 */
@Setter
@Getter
@ToString
public class NBTagVO extends NBTag {
    private String selected;
}
