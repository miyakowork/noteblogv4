package me.wuwenbin.noteblogv4.config.configuration;

import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.config.interceptor.AdminInterceptor;
import me.wuwenbin.noteblogv4.config.interceptor.ApplicationInterceptor;
import me.wuwenbin.noteblogv4.config.interceptor.SessionInterceptor;
import me.wuwenbin.noteblogv4.config.interceptor.ThemeHandlerInterceptor;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.model.constant.Upload;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * created by Wuwenbin on 2018/7/16 at 12:07
 *
 * @author wuwenbin
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final NBContext blogContext;
    private final Environment env;
    private final ParamService paramService;
    private final ParamRepository paramRepository;

    @Autowired
    public WebMvcConfig(NBContext blogContext, Environment env, ParamService paramService, ParamRepository paramRepository) {
        this.blogContext = blogContext;
        this.env = env;
        this.paramService = paramService;
        this.paramRepository = paramRepository;
    }

    /**
     * 添加一些虚拟路径的映射
     * 静态资源路径和上传文件的路径
     * 如果配置了七牛云上传，则上传路径无效
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        String uploadPath = env.getProperty("noteblog.upload.path");
        registry.addResourceHandler(Upload.FileType.VISIT_PATH + "/**").addResourceLocations(uploadPath);
    }

    /**
     * 添加一些全局的拦截器，做一些操作
     * 验证用户，设置user到session中等
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApplicationInterceptor(blogContext, paramService)).addPathPatterns("/**").excludePathPatterns("/static/**");
        registry.addInterceptor(new SessionInterceptor(blogContext)).addPathPatterns("/management/**", "/token/**");
        registry.addInterceptor(new AdminInterceptor(blogContext)).addPathPatterns("/management/**");
        registry.addInterceptor(new ThemeHandlerInterceptor(paramRepository))
                .addPathPatterns("/article/**", "/a/**").excludePathPatterns("/article/comments","/article/approve","/a/comments","/a/approve");

    }

    /**
     * 添加全局拦截的error移除处理类
     *
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> container.addErrorPages(
                new ErrorPage(HttpStatus.NOT_FOUND, "/error?errorCode=404"),
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error?errorCode=500"),
                new ErrorPage(Throwable.class, "/error?errorCode=500")
        );
    }


}
