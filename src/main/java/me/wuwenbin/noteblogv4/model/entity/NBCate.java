package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 文章分类表
 * created by Wuwenbin on 2018/7/15 at 11:49
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "nb_cate")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBCate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    private Long id;

    @Column(length = 50, nullable = false)
    @NotEmpty
    private String name;

    @Column(length = 50)
    @NotEmpty
    private String cnName;

    private String fontIcon;

}
