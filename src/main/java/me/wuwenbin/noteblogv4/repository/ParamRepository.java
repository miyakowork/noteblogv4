package me.wuwenbin.noteblogv4.repository;

import me.wuwenbin.noteblogv4.model.entity.NBParam;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2018/7/15 at 17:24
 *
 * @author wuwenbin
 */
public interface ParamRepository extends JpaRepository<NBParam, Long> {

    /**
     * 根据制定key查询对应的参数实体
     *
     * @param name
     * @return
     */
    NBParam findByNameEquals(String name);
}
