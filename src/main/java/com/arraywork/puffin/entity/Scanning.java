package com.arraywork.puffin.entity;

import java.time.LocalDateTime;

import com.arraywork.puffin.enums.ScanEvent;
import com.arraywork.puffin.enums.ScanState;

import lombok.ToString;

/**
 * 扫描数据
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@ToString
public class Scanning {

    public int count;
    public int total;

    public String path;
    public ScanEvent event;
    public ScanState state;
    public String message;
    public LocalDateTime time;

    public Scanning(ScanEvent event) {
        this.event = event;
        this.time = LocalDateTime.now();
    }

}