package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * created by Wuwenbin on 2018/8/8 at 16:46
 *
 * @author wuwenbin
 */
public interface TagRepository extends JpaRepository<NBTag, Long> {

    /**
     * 根据name查找tag对象
     *
     * @param name
     * @return
     */
    NBTag findByName(String name);

    /**
     * 查询文章/笔记的相关标签，并selected
     *
     * @param referId
     * @param type
     * @return
     */
    @Query(nativeQuery = true,
            value = "SELECT a.*, IF(COUNT(1) > 1, 'selected', '') AS selected" +
                    "        FROM ((SELECT t.* FROM nb_tag t)" +
                    "              UNION ALL" +
                    "              (SELECT t.*" +
                    "               FROM nb_tag t" +
                    "               WHERE t.id IN (SELECT tr.tag_id FROM nb_tag_refer tr WHERE tr.refer_id = ?1 AND tr.type = ?2))) a" +
                    "        GROUP BY a.`name`")
    List<Object[]> findTagListSelected(Long referId, String type);

    /**
     * 查询文章的标签
     *
     * @param referId
     * @param show
     * @return
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM nb_tag WHERE  id IN" +
                    " (SELECT tag_id FROM nb_tag_refer WHERE refer_id =?1 AND `show`= ?2 AND type = 'article')")
    List<NBTag> findArticleTags(long referId, boolean show);


}
