package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2018/7/15 at 12:11
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "nb_upload")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBUpload implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    private Long id;

    @Column(nullable = false)
    private String diskPath;

    @Column(nullable = false)
    private String virtualPath;

    private LocalDateTime upload;

    @Column(nullable = false, length = 50)
    private String type;
}
