package me.wuwenbin.noteblogv4.config.listener;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.permission.NBAuth;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.NBParam;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysResource;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Init.INIT_NOT;
import static me.wuwenbin.noteblogv4.model.constant.NoteBlogV4.Init.INIT_STATUS;

/**
 * 资源监听器
 * 检测{@code @NBAuth}注解的资源存入数据库（默认给网站管理员全部权限，否则无法访问应用）
 * created by Wuwenbin on 2018/7/19 at 22:03
 *
 * @author wuwenbin
 */
@Slf4j
@Component
public class ResourceListener implements ApplicationListener<ContextRefreshedEvent> {

    private final NBContext context;
    private final ParamRepository paramRepository;

    @Autowired
    public ResourceListener(NBContext context,
                            ParamRepository paramRepository) {
        this.context = context;
        this.paramRepository = paramRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("「笔记博客」APP 正在检测初始化状态，请稍后...");
        NBParam nbParam = paramRepository.findByName(INIT_STATUS);
        if (nbParam == null || StringUtils.isEmpty(nbParam.getValue())) {
            log.info("「笔记博客」APP 未初始化，初始化中，请稍后...");
            setUpAppInitialState();
        }else {
            log.info("「笔记博客」APP 已初始化，开始准备其它内容，请稍后...");
        }

            log.info("「笔记博客」App 正在启动中，请稍后...");
            List<Map<String, Object>> resources = new ArrayList<>(50);
            //以防万一，先移除以前的资源
            context.removeApplicationObj(NoteBlogV4.Init.MASTER_RESOURCES);
            Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Controller.class);
            beans.putAll(event.getApplicationContext().getBeansWithAnnotation(RestController.class));
            int cnt = 0;

            for (Object bean : beans.values()) {
                if (isControllerPresent(bean)) {
                    String[] prefixes = getPrefixUrl(bean);

                    for (String prefix : prefixes) {
                        Method[] methods = AopProxyUtils.ultimateTargetClass(bean).getDeclaredMethods();

                        for (Method method : methods) {
                            String[] lasts = getLastUrl(method);

                            for (String last : lasts) {
                                String url = getCompleteUrl(prefix, last);
                                if (method.isAnnotationPresent(NBAuth.class)) {
                                    RequestMethod[] requestMethods = getRequestMethod(method);
                                    String[] produces = getRequestProduces(method);
                                    log.info("资源：[url = '{}'，请求方式：'{}'，媒体类型：'{}'] 扫描到@NBAuth，准备处理...", url, Arrays.toString(requestMethods), produces);
                                    NBAuth nbAuth = method.getAnnotation(NBAuth.class);
                                    String permission = isNotEmpty(nbAuth.permission()) ? nbAuth.permission() : nbAuth.value();
                                    NBSysResource.ResType type = nbAuth.type();
                                    Map<String, Object> tempMap = MapUtil.of("permission", permission);
                                    tempMap.put("remark", nbAuth.remark());
                                    tempMap.put("url", url);
                                    tempMap.put("type", type);
                                    tempMap.put("group", nbAuth.group());
                                    resources.add(tempMap);
                                    cnt++;
                                } else {
                                    log.info("资源：[{}] 未扫描到@NBAuth，略过处理步骤", url);
                                }
                            }

                        }

                    }

                }
            }
            log.info("扫描资源完毕，共计处理资源数目：[{}]，等待下一步插入数据库赋给网站管理员角色..", cnt);
            context.setApplicationObj(NoteBlogV4.Init.MASTER_RESOURCES, resources);

        }


    /**
     * 在参数表中插入一条记录
     * 记录程序是否被初始化过（有没有在初始化界面设置过东西，设置过改为1）
     * 当然，此处是程序第一次启动，当然插入值是未初始化过的：0
     */
    private void setUpAppInitialState() {
        NBParam initStatus = NBParam.builder()
                .name(INIT_STATUS)
                .value(INIT_NOT)
                .remark("标记用户是否在「笔记博客」App 的初始化设置页面设置过")
                .level(0)
                .build();
        paramRepository.save(initStatus);
    }

    private static boolean isNotEmpty(Object str) {
        return str != null && !"".equals(str);
    }

    /**
     * 是否包含Controller或者RestController
     * 此处的ultimateTargetClass方法是用来获取被spring的cglib代理类的原始类，这样才能获取到类上面的 注解（因为cglib代理类的原理是继承原始的类成成一个子类来操作）
     *
     * @param bean
     * @return
     */
    private static boolean isControllerPresent(Object bean) {
        return AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RestController.class) || AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(Controller.class);
    }

    /**
     * 获取Controller上的RequestMapping中的value值，即url的前缀部分
     *
     * @param bean
     * @return
     */
    private static String[] getPrefixUrl(Object bean) {
        String[] prefixes;
        if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RequestMapping.class)) {
            prefixes = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(RequestMapping.class).value();
        } else {
            prefixes = new String[]{""};
        }
        return prefixes.length == 0 ? new String[]{""} : prefixes;
    }

    /**
     * 获取Controller上的RequestMapping中的method值，即请求方式
     *
     * @param bean
     * @return
     */
    private static RequestMethod[] getRequestMethod(Object bean) {
        RequestMethod[] prefixes;
        if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RequestMapping.class)) {
            prefixes = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(RequestMapping.class).method();
        } else if (bean instanceof Method && ((Method) bean).isAnnotationPresent(RequestMapping.class)) {
            prefixes = ((Method) bean).getAnnotation(RequestMapping.class).method();
        } else {
            prefixes = new RequestMethod[]{};
        }
        return prefixes.length == 0 ? new RequestMethod[]{} : prefixes;
    }

    /**
     * 获取Controller上的RequestMapping中的produces值，即请求类型
     *
     * @param bean
     * @return
     */
    private static String[] getRequestProduces(Object bean) {
        String[] prefixes;
        if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RequestMapping.class)) {
            prefixes = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(RequestMapping.class).produces();
        } else if (bean instanceof Method && ((Method) bean).isAnnotationPresent(RequestMapping.class)) {
            prefixes = ((Method) bean).getAnnotation(RequestMapping.class).produces();
        } else {
            prefixes = new String[]{""};
        }
        return prefixes.length == 0 ? new String[]{""} : prefixes;
    }

    /**
     * 获取Controller内的方法上的RequestMapping的value值，即url的后缀部分
     *
     * @param method
     * @return
     */
    private static String[] getLastUrl(Method method) {
        String[] lasts;
        if (method.isAnnotationPresent(RequestMapping.class)) {
            lasts = method.getAnnotation(RequestMapping.class).value();
        } else {
            lasts = new String[]{""};
        }
        return lasts.length == 0 ? new String[]{""} : lasts;
    }

    /**
     * 根据prefix和last获取完整url
     *
     * @param prefix
     * @param last
     * @return
     */
    private static String getCompleteUrl(String prefix, String last) {
        last = last.startsWith("/") ? last : "/".concat(last);
        return (prefix.startsWith("/") ? prefix : "/".concat(prefix)).concat("/".equals(last) ? "" : last).replaceAll("//", "/");
    }


}
