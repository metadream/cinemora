package com.arraywork.puffin.entity;

/**
 * 扫描状态
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
public class ScanStatus {

    public int count;
    public int total;
    public int success;
    public int skipped;
    public int failed;
    public String event;
    public String result; // success|skipped|failed
    public String path;
    public String message;

    // public void reset() {
    // totalFiles = 0;
    // totalMedias = 0;
    // deleted = 0;
    // inserted = 0;
    // processed = 0;
    // completed = false;
    // }

}