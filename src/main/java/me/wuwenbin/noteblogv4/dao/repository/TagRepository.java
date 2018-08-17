package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBTag;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
