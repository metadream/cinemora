package com.arraywork.cinemora.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arraywork.cinemora.entity.EventLog;

/**
 * 日志持久化
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2025/04/25
 */
public interface EventLogRepo extends JpaRepository<EventLog, String> {

    @Query(nativeQuery = true, value = """
        SELECT * FROM event_log WHERE strftime('%Y-%m-%d', time / 1000, 'unixepoch') = (
            SELECT MAX(strftime('%Y-%m-%d', time / 1000, 'unixepoch')) FROM event_log
        ) ORDER BY time DESC
        """)
    List<EventLog> findLastDayLogs();

}