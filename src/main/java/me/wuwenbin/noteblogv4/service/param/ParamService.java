package me.wuwenbin.noteblogv4.service.param;

/**
 * 参数查询Service
 * created by Wuwenbin on 2018/7/17 at 15:51
 *
 * @author wuwenbin
 */
public interface ParamService {

    /**
     * 根据key查询值
     *
     * @param name
     * @param <T>
     * @return
     */
    <T> T getValueByName(String name);
}
