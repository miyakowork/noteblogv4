package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 关于我/网站的的数据库实体类
 * created by Wuwenbin on 2018/1/20 at 16:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nb_about")
@Entity
@Builder
public class NBAbout implements Serializable {

    /**
     * 主键id
     * 自增长生成策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, length = 11)
    private Long id;

    @Column(length = 50)
    private String tab;

    @Column(length = 50)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    @NotEmpty
    private String content;

}
