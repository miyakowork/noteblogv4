package me.wuwenbin.noteblogv4.exception;

/**
 * created by Wuwenbin on 2018/8/10 at 14:26
 */
public class UserNotFoundByUUIDException extends RuntimeException {

    public UserNotFoundByUUIDException() {
        super("当前会话中未找到登录用户（用户已注销或用户登录超时！）");
    }
}
