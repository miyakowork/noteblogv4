package me.wuwenbin.noteblogv4.model.entity.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * created by Wuwenbin on 2018/7/15 at 12:04
 *
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "sys_role")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBSysRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String cnName;
}
