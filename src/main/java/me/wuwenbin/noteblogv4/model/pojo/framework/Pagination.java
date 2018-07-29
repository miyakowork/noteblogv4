package me.wuwenbin.noteblogv4.model.pojo.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 * Created by wuwenbin on 2015/9/15.
 *
 * @author wuwenbin
 * @since 1.0.0
 */
public class Pagination<T> extends PageOrder implements Serializable {

    /**
     * 当前页面编号，默认1
     */
    private int page = 1;

    /**
     * 每页数据量大小，默认30
     */
    private int limit = 30;


    /**
     * 当前页的数据集合List
     */
    private List result = null;


    /**
     * 总数据量大小，默认-1
     */
    private int totalCount = -1;

    private boolean autoCount = true;


    //some construct methods

    public Pagination() {
    }

    public Pagination(int limit) {
        this.limit = limit;
    }

    public Pagination(List<T> data) {
        this.result = data;
    }

    public Pagination(int limit, String orderDirection, String orderField) {
        this.limit = limit;
        super.order = orderDirection;
        super.sort = orderField;
    }

    public boolean isAutoCount() {
        return autoCount;
    }

    public void setAutoCount(boolean autoCount) {
        this.autoCount = autoCount;
    }

    //some methods for page

    /**
     * @return 每页数据量大小
     */
    public int getLimit() {
        return limit;
    }

    public int getPageSize() {
        return limit;
    }

    /**
     * 设置每页数据量大小
     *
     * @param #limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPageSize(int pageSize) {
        setLimit(pageSize);
    }

    /**
     * @return 是否已经设置每页数据量大小
     */
    public boolean isPageSizeSetted() {
        return limit >= 0;
    }

    /**
     * @return 获取当前页码
     */
    public int getPage() {
        return getTotalPages() != -1 && getTotalPages() <= page ? getTotalPages() : page;
    }

    /**
     * 和 {@link #getPage()}一致
     *
     * @see #getPage()
     */
    public int getCurrentPageNo() {
        return getPage();
    }

    public int getPageNo() {
        return getPage();
    }

    /**
     * 设置当前页码
     *
     * @param #page
     */
    public void setPage(int page) {
        this.page = page;
    }

    public void setPageNo(int pageNo) {
        setPage(pageNo);
    }

    /**
     * @return 通过挡圈的页码和页面数据量大小计算当前页面的数据的第一条在数据库中的顺序
     */
    public int getFirst() {
        return getPage() < 1 || getLimit() < 1 ? -1 : (getPage() - 1) * getLimit();
    }

    /**
     * @return whether page is set the first
     */
    public boolean isFirstSetted() {
        return (getPage() > 0 && getLimit() > 0);
    }

    /**
     * @return transfer data of the current page
     */
    public List<?> getResult() {
        return result == null ? new ArrayList<>() : result;
    }

    /**
     * 获取泛型结果
     *
     * @return
     */
    public List<T> getTResult() {
        return result == null ? new ArrayList<T>() : result;
    }

    /**
     * 获取原生不带类型的结果
     *
     * @return
     */
    public List getRawResult() {
        return result == null ? new ArrayList<>() : result;
    }

    /**
     * @param #result
     */
    public void setTResult(List<T> result) {
        this.result = result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

    public void setRawResult(List result) {
        this.result = result;
    }

    /**
     * @return 获取所有数据量大小
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 所有数据量大小
     *
     * @param #totalCount
     */
    public void setTotalCount(long totalCount) {
        this.totalCount = (int) totalCount;
    }

    /**
     * @return 计算页面数量，总共多少页
     */
    public int getTotalPages() {
        if (totalCount == -1) {
            return -1;
        }
        int count = totalCount / limit;
        if (totalCount % limit > 0) {
            count++;
        }
        return count;
    }

    /**
     * @return 是否有下一页
     */
    public boolean isHasNext() {
        return (page + 1 <= getTotalPages());
    }

    /**
     * @return 下一页的页码
     */
    public int getNextPage() {
        return isHasNext() ? page + 1 : page;
    }

    /**
     * @return 是否有上一页
     */
    public boolean isHasPre() {
        return (page - 1 >= 1);
    }

    /**
     * @return 上一页页码
     */
    public int getPrePage() {
        return isHasPre() ? page - 1 : page;
    }
}
