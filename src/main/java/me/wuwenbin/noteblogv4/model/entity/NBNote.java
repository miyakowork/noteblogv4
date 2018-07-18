package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;

/**
 * created by Wuwenbin on 2018/7/15 at 12:00
 */
@Data
@Entity
@Table(name = "nb_note")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBNote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 999)
    private String clearContent;

    @Column(length = 999, nullable = false)
    private String content;

    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1)")
    @Builder.Default
    private Boolean top = FALSE;

    @Column(nullable = false, length = 1, name = "[note]")
    @Builder.Default
    private Boolean show = TRUE;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime post = now();

    private LocalDateTime modify;
}
