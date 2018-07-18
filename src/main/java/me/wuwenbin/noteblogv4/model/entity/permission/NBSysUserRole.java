package me.wuwenbin.noteblogv4.model.entity.permission;

import lombok.*;
import me.wuwenbin.noteblogv4.model.entity.permission.pk.UserRoleKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * created by Wuwenbin on 2018/7/18 at 14:04
 *
 * @author wuwenbin
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sys_user_role")
public class NBSysUserRole implements Serializable {

    @EmbeddedId
    private UserRoleKey pk;

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    @Builder.Default
    private Boolean enable = Boolean.TRUE;

}
