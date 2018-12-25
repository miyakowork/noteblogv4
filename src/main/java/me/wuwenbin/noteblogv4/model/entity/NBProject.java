package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2018/12/18 at 23:09
 *
 * @author wuwenbin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nb_project")
@Entity
@Builder
public class NBProject implements Serializable {

    /**
     * 主键id
     * 自增长生成策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, length = 11)
    private Long id;

    @Column(length = 11, nullable = false)
    @NotNull(message = "项目必须属于一个分类下")
    private Long cateId;

    @ManyToOne
    @JoinColumn(name = "cate_refer_id")
    @NotNull
    private NBProjectCate projectCate;

    @Column(nullable = false)
    private String cover;

    private LocalDateTime post;

    private LocalDateTime modify;

    @NotEmpty(message = "名称不能为空")
    @Column(nullable = false, length = 11)
    private String name;

    @NotEmpty(message = "描述不能为空")
    private String description;

    @NotEmpty(message = "主页地址不能为空")
    private String url;

}
