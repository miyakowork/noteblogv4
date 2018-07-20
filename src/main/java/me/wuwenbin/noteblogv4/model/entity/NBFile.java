package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

/**
 * created by Wuwenbin on 2018/7/15 at 11:55
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "nb_file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    public Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String url;

    @Builder.Default
    private LocalDateTime post = now();
}
