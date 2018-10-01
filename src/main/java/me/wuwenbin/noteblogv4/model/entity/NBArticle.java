package me.wuwenbin.noteblogv4.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.randomInt;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;

/**
 * 博文的表对应的实体类
 * created by Wuwenbin on 2018/7/15 at 10:57
 *
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "nb_article")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NBArticle implements Serializable {

    /**
     * 主键id
     * 生成策略为自定义的id生成策略
     */
    @Id
    @Column(length = 20, updatable = false, nullable = false)
    @GeneratedValue(generator = "articleId")
    @GenericGenerator(name = "articleId", strategy = "me.wuwenbin.noteblogv4.model.strategy.NBArticleStrategy")
    private Long id;

    @Column(length = 100, nullable = false)
    @NotEmpty(message = "文章标题不能为空")
    private String title;

    @Column(length = 11, nullable = false)
    @NotNull(message = "文章必须属于一个分类下")
    private Long cateId;

    @ManyToOne
    @JoinColumn(name = "cate_refer_id")
    private NBCate cate;

    private String cover;

    @Column(length = 300)
    private String summary;

    @Column(columnDefinition = "mediumtext", nullable = false)
    @NotEmpty(message = "文章内容不能为空")
    private String content;

    @Column(columnDefinition = "mediumtext")
    @Builder.Default
    private String mdContent = "";

    @Column(columnDefinition = "mediumtext")
    @Builder.Default
    private String textContent = "";

    @Column(length = 11, updatable = false)
    private Long authorId;

    @Column(nullable = false, updatable = false, length = 0)
    @Builder.Default
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime post = now();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modify;

    @Column(length = 11, nullable = false)
    @Builder.Default
    private Integer view = randomInt(666, 1609);

    @Column(length = 11, nullable = false)
    @Builder.Default
    private Integer approveCnt = randomInt(6, 169);

    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1) default 0")
    @Builder.Default
    private Boolean commented = FALSE;

    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1) default 0")
    @Builder.Default
    private Boolean appreciable = FALSE;

    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1) default 1")
    @Builder.Default
    private Boolean draft = TRUE;

    @Column(length = 11)
    @Builder.Default
    private Integer top = 0;

    /**
     * url自定义链接标识部分
     */
    @Column(name = "url_seq", length = 100)
    private String urlSequence;


}
