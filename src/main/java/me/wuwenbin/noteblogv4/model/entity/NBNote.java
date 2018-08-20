package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;

/**
 * created by Wuwenbin on 2018/7/15 at 12:00
 *
 * @author wuwenbin
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
    @NotEmpty
    private String title;

    @Column(columnDefinition = "text")
    private String clearContent;

    @Column(columnDefinition = "text")
    private String mdContent;

    @Column(nullable = false, columnDefinition = "text")
    @NotEmpty
    private String content;

    @Column(nullable = false, length = 1, columnDefinition = "tinyint(1)")
    @Builder.Default
    private Boolean top = FALSE;

    @Column(nullable = false, length = 1, name = "[show]", columnDefinition = "tinyint(1)")
    @Builder.Default
    private Boolean show = TRUE;

    @Column(nullable = false)
    @Builder.Default
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime post = now();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modify;
}
