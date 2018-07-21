package me.wuwenbin.noteblogv4.config.configuration;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * 访问数据库的一些配置
 * created by Wuwenbin on 2018/7/14 at 9:26
 *
 * @author wuwenbin
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    private final Environment env;

    @Autowired
    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    /**
     * 配置数据源
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        try {
            String ip = env.getProperty("db.ip", "127.0.0.1");
            String port = env.getProperty("db.port", "3306");
            String name = env.getProperty("db.name", "noteblogv4");
            String user = env.getProperty("db.username", "root");
            String pass = env.getProperty("db.password", "123456");
            DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
            String url = StrUtil.format("jdbc:mysql://{}:{}/{}?useUnicode=true&characterEncoding=UTF-8&useSSL=true", ip, port, name);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(user);
            druidDataSource.setPassword(pass);
            return druidDataSource;
        } catch (Exception e) {
            log.error("初始化数据源出错，错误信息：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
