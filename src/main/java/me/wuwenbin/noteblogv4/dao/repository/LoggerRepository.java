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
     * @return
     */
    @Query(nativeQuery = true,
            value = "select DATE_FORMAT(time,   '%Y-%m-%d') from sys_logger group by DATE_FORMAT(time,   '%Y-%m-%d') order by time desc limit 5")
    List<NBLogger> findLatest5Dates();

    /**
     * 查询数量最多的4个项目，连同数量一并查出
     * 结果类似如下：
     * http://127.0.0.1:8000/management/dashboard	84
     * http://127.0.0.1:8000/management/index	74
     * http://127.0.0.1:8000/login	24
     * http://127.0.0.1:8000/image/code	14
     *
     * @param selectItem
     * @param dateStr
     * @return
     */
    @Query(nativeQuery = true,
            value = "select ?1,count(?1) from sys_logger where time like ?2 group by ?1 order by count(?1) desc limit 4")
    List<Object[]> findXxxData(String selectItem, String dateStr);
}
