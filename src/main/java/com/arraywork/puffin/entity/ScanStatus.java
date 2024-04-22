package com.arraywork.puffin.entity;

/**
 * 扫描状态
 * @author AiChen
 * @created 2024/04/21
 */
public class ScanStatus {

    public int totalFiles;
    public int totalMedias;
    public int deleted;
    public int inserted;
    public int processed;
    public boolean completed;

    public void reset() {
        totalFiles = 0;
        totalMedias = 0;
        deleted = 0;
        inserted = 0;
        processed = 0;
        completed = false;
    }

}