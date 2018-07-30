package me.wuwenbin.noteblogv4.service.login;

import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;

/**
 * 登录服务接口
 * created by Wuwenbin on 2018/7/21 at 17:59
 * @author wuwenbin
 */
public interface LoginService<T> {


    /**
     * 登录方法
     *
     * @param data
     * @return
     */
    NBR doLogin(T data);


}
