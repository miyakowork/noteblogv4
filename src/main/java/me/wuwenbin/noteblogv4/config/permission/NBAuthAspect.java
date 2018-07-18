package me.wuwenbin.noteblogv4.config.permission;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.util.NBUtils;
import me.wuwenbin.noteblogv4.web.BaseController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

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

    @Pointcut("@annotation(me.wuwenbin.noteblogv4.config.permission.NBAuth)")
    public void authority() {
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
        boolean restController = signature.getDeclaringType().isAnnotationPresent(RestController.class);
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        NBAuth nbAuth = targetMethod.getAnnotation(NBAuth.class);
        log.info("进入验权步骤，需要权限：[{}]...", nbAuth.value());
        try {
            NBSysUser user = NBUtils.getSessionUser();
            if (user != null) {
                return pjp.proceed();
            } else {
                boolean responseBody = targetMethod.isAnnotationPresent(ResponseBody.class);
                //粗糙的判断为ajax请求
                if (restController || responseBody) {
                    return null;
                } else if (isRouter(NBUtils.getCurrentRequest())) {
                    return "error/router";
                } else {
                    return "error/page";
                }
            }
        } catch (Throwable throwable) {
            log.error("验权过程中出现异常，异常信息：{}", throwable.getMessage());
            throw new RuntimeException(throwable);
        }
    }
}
