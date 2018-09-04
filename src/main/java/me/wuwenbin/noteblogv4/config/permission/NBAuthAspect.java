package me.wuwenbin.noteblogv4.config.permission;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ResourceRepository;
import me.wuwenbin.noteblogv4.exception.UnauthorizedRoleException;
import me.wuwenbin.noteblogv4.exception.UserNotLoginException;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import me.wuwenbin.noteblogv4.util.NBUtils;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过{@code @NBAuth}注解来验证权限
 * created by Wuwenbin on 2018/7/18 at 15:50
 *
 * @author wuwenbin
 */
@Slf4j
@Aspect
@Component
public class NBAuthAspect extends BaseController {

    private final ResourceRepository resourceRepository;

    @Autowired
    public NBAuthAspect(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    /**
     * 权限验证切点
     */
    @Pointcut("@annotation(me.wuwenbin.noteblogv4.config.permission.NBAuth)")
    public void authority() {
    }

    /**
     * 角色验证切点
     */
    @Pointcut("@annotation(me.wuwenbin.noteblogv4.config.permission.NBAuthRole)")
    public void authorityRole() {
    }


    /**
     * 校验权限是否满足
     *
     * @param pjp
     * @return
     */
    @Around("authority()")
    public Object authPermission(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        NBAuth nbAuth = targetMethod.getAnnotation(NBAuth.class);
        boolean restController = signature.getDeclaringType().isAnnotationPresent(RestController.class);
        boolean responseBody = targetMethod.isAnnotationPresent(ResponseBody.class);
        String requestUrl = NBUtils.getCurrentRequest().getRequestURL().toString();
        log.info("请求资源：[{}]，验证权限中，需要权限：[{}]...", requestUrl, nbAuth.value());
        try {
            NBSysUser user = NBUtils.getSessionUser();
            if (user != null) {
                //TODO:权限后续记得加上缓存
                List<NBSysResource> resources = resourceRepository.findResourcesByRoleId(user.getDefaultRoleId());
                List<String> permissions = resources.stream().map(NBSysResource::getPermission).collect(Collectors.toList());
                if (permissions.contains(nbAuth.value())) {
                    log.info("验证权限通过，放行...");
                    return pjp.proceed();
                } else {
                    log.info("验证权限失败，跳转提示...");
                    return handleAuthNotPass(restController, responseBody);
                }
            } else {
                log.info("验证权限失败，用户未登录或者登录过期，跳转提示...");
                return handleUserNotLogin(restController, responseBody);
            }
        } catch (Throwable throwable) {
            log.error("验权过程中出现异常，异常信息：{}", throwable.getMessage());
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 校验角色是否满足
     *
     * @param pjp
     * @return
     */
    @Around("authorityRole()")
    public Object authRole(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        boolean restController = signature.getDeclaringType().isAnnotationPresent(RestController.class);
        boolean responseBody = targetMethod.isAnnotationPresent(ResponseBody.class);
        NBAuthRole nbAuthRole = targetMethod.getAnnotation(NBAuthRole.class);
        log.info("请求资源：[{}]，验证用户角色中，需要角色：[{}]...", Arrays.toString(nbAuthRole.value()));
        try {
            NBSysUser user = NBUtils.getSessionUser();
            if (user != null) {
                //判断用户当前角色（默认角色）是否符合该要求
                if (ArrayUtil.contains(nbAuthRole.value(), user.getDefaultRoleId())) {
                    log.info("验证角色通过，放行...");
                    return pjp.proceed();
                } else {
                    log.info("验证角色失败，跳转提示...");
                    return handleAuthNotPass(restController, responseBody);
                }
            } else {
                log.info("验证角色失败，用户未登录或者登录过期，跳转提示...");
                return handleUserNotLogin(restController, responseBody);
            }
        } catch (Throwable throwable) {
            log.error("验权过程中出现异常，异常信息：{}", throwable.getMessage());
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 设置出错属性
     *
     * @param status
     * @param time
     * @param error
     */
    private void setErrorAttribute(int status, String time, Throwable error) {
        HttpServletRequest request = NBUtils.getCurrentRequest();
        request.setAttribute("status", status);
        request.setAttribute("timestamp", time);
        request.setAttribute("error", error);
        request.setAttribute("message", error.getMessage());
        request.setAttribute("path", request.getRequestURL());
    }

    /**
     * 处理用户未登录或超时的验权未通过的情况
     *
     * @param restController
     * @param responseBody
     * @return
     */
    private Object handleUserNotLogin(boolean restController, boolean responseBody) {
        //粗糙的判断为ajax请求
        if (restController || responseBody) {
            return NBR.error("未登录或登录过期，请求失败！");
        } else if (isRouter(NBUtils.getCurrentRequest())) {
            Exception e = new UserNotLoginException("未登录或登录过期，无法访问！");
            setErrorAttribute(402, LocalDateTime.now().toString(), e);
            return "error/router_login";
        } else {
            Exception e = new UserNotLoginException("未登录或登录过期，无法访问！");
            setErrorAttribute(402, LocalDateTime.now().toString(), e);
            return "error/page_login";
        }
    }

    /**
     * 处理用户不包含该权限的情况，未通过验证
     *
     * @param restController
     * @param responseBody
     * @return
     */
    private Object handleAuthNotPass(boolean restController, boolean responseBody) {
        //粗糙的判断为ajax请求
        if (restController || responseBody) {
            return NBR.error("您所在的用户组没有权限！");
        } else if (isRouter(NBUtils.getCurrentRequest())) {
            Exception e = new UnauthorizedRoleException("该资源未授权给当前用户的角色，无法访问！");
            setErrorAttribute(401, LocalDateTime.now().toString(), e);
            return "error/router";
        } else {
            Exception e = new UnauthorizedRoleException("该资源未授权给当前用户的角色，无法访问！");
            setErrorAttribute(401, LocalDateTime.now().toString(), e);
            return "error/page";
        }
    }
}
