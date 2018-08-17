package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

import static java.lang.Boolean.TRUE;

/**
 * created by Wuwenbin on 2018/7/15 at 11:57
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "nb_keyword")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBKeyword implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    private Long id;

    @Column(nullable = false)
    @NotEmpty
    private String words;

    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1)")
    @Builder.Default
    private Boolean enable = TRUE;
}
