package me.wuwenbin.noteblogv4;

import me.wuwenbin.noteblogv4.dao.annotation.MybatisDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author wuwenbin
 */
@SpringBootApplication
@MapperScan(basePackages = "me.wuwenbin.noteblogv4.dao.mapper", annotationClass = MybatisDao.class)
public class NoteBlogV4Application {

    public static void main(String[] args) {
        SpringApplication.run(NoteBlogV4Application.class, args);
    }

}
