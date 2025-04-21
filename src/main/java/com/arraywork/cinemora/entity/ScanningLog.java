package com.arraywork.cinemora.entity;

import java.time.LocalDateTime;

import com.arraywork.cinemora.enums.ScanningAction;
import com.arraywork.cinemora.enums.ScanningResult;

import lombok.Data;

/**
 * 扫描日志
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2025/04/20
 */
@Data
public class ScanningLog {

    private long ordinal;
    private long total;
    private long indexed;
    private long reindexed;
    private long skipped;
    private long failed;
    private long percent;

    private String message;
    private ScanningAction action;
    private ScanningResult result;
    private LocalDateTime time = LocalDateTime.now();

    public long getPercent() {
        return total > 0 && total >= ordinal ? 100 * ordinal / total : -1;
    }

}