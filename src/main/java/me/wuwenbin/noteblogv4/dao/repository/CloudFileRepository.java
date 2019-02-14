package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBCloudFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Wuwenbin on 2019/2/13 at 16:33
 *
 * @author wuwenbin
 */
public interface CloudFileRepository extends JpaRepository<NBCloudFile, Long> {

    /**
     * 计算某个cateId说下的数量
     *
     * @param cateId
     * @return
     */
    long countByCateId(long cateId);

}
