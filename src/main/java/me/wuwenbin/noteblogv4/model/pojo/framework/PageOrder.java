package me.wuwenbin.noteblogv4.model.pojo.framework;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.StringUtils;

/**
 * 分页排序对象
 * Created by wuwenbin on 2014/11/1
 *
 * @author wuwenbin
 * @since 1.0.0
 */
public class PageOrder {

    /**
     * 排序字段
     */
    protected String sort;

    /**
     * 排序方式
     */
    protected String order;


    /**
     * getters and setters
     */

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderBy() {
        if (!StringUtils.isEmpty(this.sort) && !StringUtils.isEmpty(this.order)) {
            return StrUtil.format("`{}` {}", this.sort, this.order);
        }
        return "";
    }

    public void setOrderBy(String orderBy) {
        if (!StringUtils.isEmpty(orderBy)) {
            String[] temp = orderBy.split(" ");
            int expectLength = 2;
            if (temp.length == expectLength) {
                this.sort = temp[0].replace("`", "");
                this.order = temp[1];
            }
        }
    }

}
