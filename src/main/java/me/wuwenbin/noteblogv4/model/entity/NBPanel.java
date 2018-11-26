package me.wuwenbin.noteblogv4.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 首页面板对象
 * 方便用户定义面板的顺序以及面板的标题
 * created by Wuwenbin on 2018/7/19 at 下午2:43
 *
 * @author wuwenbin
 */
@Data
@Entity
@Table(name = "nb_panel")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NBPanel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, length = 11)
    private Long id;

    /**
     * 表明是存储name到数据库
     */
    @Enumerated(EnumType.STRING)
    private PanelDom panelDom;

    @Column(nullable = false, length = 5)
    private String titleName;

    @Column(nullable = false, length = 3)
    private Integer orderIndex;

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    @Builder.Default
    private Boolean enable = Boolean.TRUE;

    /**
     * 在网页上的panel dom 对象
     */
    public enum PanelDom {
        /**
         * 显示网站信息的面板
         */
        infoPanel("信息面板", 0),

        /**
         * 搜索面板
         */
        searchPanel("搜索库", 1),

        /**
         * 功能中心面板
         */
        functionPanel("功能区", 2),

        /**
         * 分类面板
         */
        catePanel("分类堆", 3),

        /**
         * 随机文章列表面板
         */
        randomPanel("博文栈", 4),

        /**
         * 标签面板
         */
        tagPanel("标签页", 5),

        /**
         * 友情链接面板
         */
        linkPanel("友链区", 6);

        private String titleName;
        private int order;

        PanelDom(String titleName, int order) {
            this.titleName = titleName;
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

        public String getTitleName() {
            return titleName;
        }

    }
}
