package me.wuwenbin.noteblogv4.config.listener;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.exception.InitException;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2018/8/2 at 20:35
 *
 * @author wuwenbin
 */
@Slf4j
@Component
@Order(4)
public class HelloWorldListener implements ApplicationListener<ApplicationReadyEvent> {

    private final CateRepository cateRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public HelloWorldListener(CateRepository cateRepository, ArticleRepository articleRepository) {
        this.cateRepository = cateRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (cateRepository.count() == 0 && articleRepository.count() == 0) {
            log.info("初始化「笔记博客」APP 基本内容，请稍后...");
            long cateId = setFirstCategory();
            setHelloWorldArticle(cateId);
            log.info("初始化「笔记博客」APP 基本内容完毕");
        }
    }


    /**
     * 设置默认文章（HelloWorld）的分类
     *
     * @return
     */
    private Long setFirstCategory() {
        NBCate defaultCate = NBCate.builder()
                .cnName("默认分类")
                .name("def_cate")
                .fontIcon("fa fa-sliders")
                .build();
        defaultCate = cateRepository.save(defaultCate);
        if (defaultCate.getId() != null) {
            return defaultCate.getId();
        }
        throw new InitException("初始化默认文章分类出错");
    }

    private void setHelloWorldArticle(long cateId) {
        final String title = "这个是文章的标题a a aa啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊";
        final String text = "偶遇一个注入，权限还是蛮大的，至少执行cmdshell是没问题的，因为这是一台数据库服务器，和目标站是站库分离，又在内网中，所以没办法拿下shell。我想得到内网环境，继续内网渗透，可是在用sqlmap上传的过程中发现始终无法上传";
        NBArticle helloWorldArticle = NBArticle.builder()
                .authorId(1L)
                .cateId(cateId)
                .content(text)
                .textContent(text)
                .cate(cateRepository.getOne(cateId))
                .summary(text)
                .post(LocalDateTime.now())
                .title(title).build();
        articleRepository.save(helloWorldArticle);
    }

}
