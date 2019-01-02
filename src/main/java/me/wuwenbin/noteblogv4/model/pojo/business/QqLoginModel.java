package me.wuwenbin.noteblogv4.model.pojo.business;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2019/1/2 at 16:57
 *
 * @author wuwenbin
 */
@Data
@Builder
public class QqLoginModel implements Serializable {

    private String callbackDomain;
    private String code;
}
