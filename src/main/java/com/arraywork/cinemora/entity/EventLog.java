package com.arraywork.cinemora.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.arraywork.autumn.id.NanoIdGeneration;
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
@Entity
@Data
public class EventLog {

    @Id
    @NanoIdGeneration
    @Column(length = 24, insertable = false, updatable = false)
    private String id;

    private long count;
    private long total;
    private long indexed;
    private long reindexed;
    private long deleted;
    private long skipped;
    private long failed;

    private String path;
    private String hint;
    private EventSource source;
    private EventState state;

    @CreationTimestamp
    private LocalDateTime time;

    @Transient
    private long progress;

    public long getProgress() {
        return total > 0 && total >= count ? 100 * count / total : -1;
    }

}