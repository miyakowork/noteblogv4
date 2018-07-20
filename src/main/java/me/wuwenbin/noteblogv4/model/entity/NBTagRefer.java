package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * created by Wuwenbin on 2018/7/15 at 12:07
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "nb_tag_refer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBTagRefer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    private Long id;

    @Column(nullable = false, length = 11)
    private Long referId;

    @Column(nullable = false, length = 11)
    private Long tagId;

    @Column(nullable = false, length = 1, name = "[show]", columnDefinition = "tinyint(1)")
    private Boolean show;

    @Column(nullable = false, length = 50)
    private String type;
}
