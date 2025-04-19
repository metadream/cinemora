package com.arraywork.cinemora.entity;

import java.time.LocalDateTime;

import com.arraywork.cinemora.enums.ScanEvent;
import com.arraywork.cinemora.enums.ScanState;

import lombok.Data;

/**
 * 扫描数据
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@Data
public class ScanningInfo {

    public int count;
    public int total;

    public String path;
    public ScanEvent event;
    public ScanState state;
    public String message;
    public LocalDateTime time;

    public ScanningInfo(ScanEvent event) {
        this.event = event;
        this.time = LocalDateTime.now();
    }

}