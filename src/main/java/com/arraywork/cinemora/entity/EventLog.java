package com.arraywork.cinemora.entity;

import java.time.LocalDateTime;

import com.arraywork.cinemora.enums.EventSource;
import com.arraywork.cinemora.enums.EventState;

import lombok.Data;

/**
 * 事件日志
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2025/04/20
 */
@Data
public class EventLog {

    private long ordinal;
    private long total;
    private long progress;

    private long indexed;
    private long reindexed;
    private long skipped;
    private long failed;

    private String message;
    private EventSource source;
    private EventState state;
    private LocalDateTime time = LocalDateTime.now();

    public long getProgress() {
        return total > 0 && total >= ordinal ? 100 * ordinal / total : -1;
    }

}