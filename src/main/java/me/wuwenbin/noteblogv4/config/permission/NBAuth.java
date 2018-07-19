package me.wuwenbin.noteblogv4.config.permission;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 基于注解的权限简单验证
 * 此处为权限注解
 * created by Wuwenbin on 2018/7/18 at 15:37
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NBAuth {

    /**
     * 权限标识
     *
     * @return 该资源的权限标识，唯一
     */
    String value();

    @AliasFor("value")
    String permission() default "";

    /**
     * 该权限标识的意义
     *
     * @return 说明
     */
    String remark() default "";
}
