package me.wuwenbin.noteblogv4.model.entity.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;

/**
 * created by Wuwenbin on 2018/7/14 at 10:37
 *
 * @author wuwenbin
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@Table(name = "sys_user")
@NoArgsConstructor
public class NBSysUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, length = 11)
    private Long id;

    @Column(length = 20)
    private String username;

    @Column(length = 50)
    private String nickname;

    @Column(length = 50)
    private String avatar;

    private String password;

    @Column(updatable = false, name = "[create]")
    @Builder.Default
    private LocalDateTime create = now();

    @Column(length = 20)
    private String qqNum;

    /**
     * 默认为普通访客用户
     * 一个用户可能有多个角色，此字段为默认角色
     */
    @Column(nullable = false, length = 11)
    @Builder.Default
    private Long defaultRoleId = 2L;

    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1)")
    @Builder.Default
    private Boolean enable = TRUE;

    @Column(length = 32)
    private String openId;

}
