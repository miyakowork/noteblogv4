package me.wuwenbin.noteblogv4;

import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 欢迎加入我们，QQ群：697053454
 * @author wuwenbin
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "me.wuwenbin.noteblogv4.dao.mapper", annotationClass = MybatisDao.class)
@EnableCaching
public class NoteBlogV4Application {

    public static void main(String[] args) {
        SpringApplication.run(NoteBlogV4Application.class, args);
    }

}
