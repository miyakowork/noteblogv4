package me.wuwenbin.noteblogv4.model.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Wuwenbin on 2018/7/23 at 16:55
 *
 * @author wuwenbin
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LayuiXTree implements Serializable {

    private String title;
    private String value;
    private Boolean checked;
    private Boolean disabled;
    @Builder.Default
    private List<LayuiXTree> data = new ArrayList<>();
}
