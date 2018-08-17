package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * created by Wuwenbin on 2018/7/15 at 12:06
 *
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "nb_tag", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBTag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    private Long id;

    @Column(nullable = false, length = 50)
    @NotEmpty
    private String name;
}
