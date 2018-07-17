package me.wuwenbin.noteblogv4.model.pojo.framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络传输对象
 * created by Wuwenbin on 2017/12/20 at 下午3:39
 *
 * @author wuwenbin
 * @since 1.10.5.RELEASE
 */
public class R extends ConcurrentHashMap<String, Object> {

    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";

    public static final int SUCCESS = 200;
    public static final int SERVER_ERROR = 500;

    private R() {
        put(CODE, SUCCESS);
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 返回默认的成功响应的实体
     *
     * @return
     */
    public static R ok() {
        return new R();
    }

    /**
     * 返回默认的成功响应的实体，只带文本信息
     *
     * @param msg 文本信息
     * @return
     */
    public static R ok(String msg) {
        R r = new R();
        r.put("message", msg == null || "".equals(msg) ? "success!" : msg);
        return r;
    }

    /**
     * 自定义成功响应数据，包含额外的返回数据
     *
     * @param data 额外的数据
     * @return 返回响应正确的实体
     */
    public static R ok(Object data) {
        return ok(null, data);
    }

    /**
     * 自定义成功响应数据，包含响应信息以及额外的返回数据
     *
     * @param msg  响应信息
     * @param data 额外的西湖局
     * @return 返回响应正确的实体
     */
    public static R ok(String msg, Object data) {
        return ok(msg).put("data", data);
    }

    /**
     * 自定义返回成功信息，带额外的map信息
     *
     * @param jsonMap map信息
     * @return
     */
    public static R ok(Map<String, Object> jsonMap) {
        R r = new R();
        r.putAll(jsonMap);
        return r;
    }

    /**
     * 自定义错误响应数据，带额外的数据（指定类型，默认状态码为500
     *
     * @param message 错误信息
     * @param <T>     额外数据指定的类型
     * @return 返回响应错误的实体
     */
    public static <T> R error(String message, T data) {
        return error(message).put("data", data);
    }

    /**
     * 自定义错误响应数据，默认状态码为500
     *
     * @param message 错误信息
     * @return 返回响应错误的实体
     */
    public static R error(String message) {
        return ok().put("code", SERVER_ERROR).put("message", message);
    }

    /**
     * 自定义响应数据，不带额外的参数
     *
     * @param code    状态码
     * @param message 响应信息
     * @return 静态方法，返回响应实体JSON数据
     */
    public static R custom(int code, String message) {
        return ok().put("code", code).put("message", message);
    }

    /**
     * 自定义响应数据，带额外的参数（指定类型）
     *
     * @param code    状态码
     * @param message 响应信息
     * @param data    额外的数据
     * @param <T>     额外数据的指定类型
     * @return 静态方法，返回响应实体JSON数据
     */
    public static <T> R custom(int code, String message, T data) {
        return custom(code, message).put("data", data);
    }
}
