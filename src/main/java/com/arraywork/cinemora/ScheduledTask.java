package com.arraywork.cinemora;

import jakarta.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arraywork.cinemora.service.EventLogService;

/**
 * 定时任务
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2025/04/25
 */
@Component
public class ScheduledTask {

    @Resource
    private EventLogService logService;

    @Scheduled(cron = "${app.schedule.clear-logs:-}")
    public void clearEventLogs() {
        logService.clear();
    }

}