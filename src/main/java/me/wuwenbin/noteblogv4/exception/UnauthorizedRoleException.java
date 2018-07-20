package me.wuwenbin.noteblogv4.exception;

/**
 * 角色未授权
 * created by Wuwenbin on 2018/7/20 at 14:22
 *
 * @author wuwenbin
 */
public class UnauthorizedRoleException extends RuntimeException {
    public UnauthorizedRoleException() {
        super();
    }

    public UnauthorizedRoleException(String message) {
        super(message);
    }
}
