package com.arraywork.cinemora.service;

import java.util.List;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.cinemora.entity.EventLog;
import com.arraywork.cinemora.repo.EventLogRepo;

/**
 * 日志服务
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/05/02
 */
@Service
public class EventLogService {

    @Resource
    private EventLogRepo logRepo;

    /** 获取日志 */
    public List<EventLog> getLogs() {
        return logRepo.findLastDayLogs();
    }

    /** 保存日志 */
    @Transactional(rollbackFor = Exception.class)
    public EventLog save(EventLog log) {
        return logRepo.save(log);
    }

}