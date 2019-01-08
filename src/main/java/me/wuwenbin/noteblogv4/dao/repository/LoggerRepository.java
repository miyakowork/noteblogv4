package me.wuwenbin.noteblogv4.dao.repository;

import me.wuwenbin.noteblogv4.model.entity.NBLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * created by Wuwenbin on 2018/7/21 at 17:46
 *
 * @author wuwenbin
 */
public interface LoggerRepository extends JpaRepository<NBLogger, Long> {

    /**
     * 统计ip数量
     *
     * @return
     */
    @Query(nativeQuery = true, value = "select count(distinct ip_addr) from sys_logger")
    int countByIpAddr();

    /**
     * 统计某天的ip数量
     *
     * @param time
     * @return
     */
    @Query(nativeQuery = true, value = "select count(distinct ip_addr) from sys_logger where time like ?1")
    int countByIpAddrAndTimeLike(String time);

    /**
     * 找出有记录的最近的5天
     *
     * @param dayDuring
     * @return
     */
    @Query(nativeQuery = true,
            value = "select DATE_FORMAT(time, '%Y-%m-%d') ,count(*) as  cnt from sys_logger group by DATE_FORMAT(time, '%Y-%m-%d') order by time desc limit ?1")
    List<Object[]> findTableData(int dayDuring);


}
