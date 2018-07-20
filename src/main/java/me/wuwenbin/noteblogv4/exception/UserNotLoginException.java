package me.wuwenbin.noteblogv4.exception;

/**
 * 用户未登录或超时
 * created by Wuwenbin on 2018/7/20 at 14:22
 *
 * @author wuwenbin
 */
public class UserNotLoginException extends RuntimeException {
    public UserNotLoginException() {
        super();
    }

    public UserNotLoginException(String message) {
        super(message);
    }
}
