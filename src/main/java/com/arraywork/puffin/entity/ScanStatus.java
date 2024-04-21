package com.arraywork.puffin.entity;

import lombok.Data;

/**
 * 扫描状态
 * @author AiChen
 * @created 2024/04/21
 */
@Data
public class ScanStatus {

    private int totalFiles;
    private int totalMovies;
    private int deleted;
    private int inserted;
    private int processed;
    private boolean completed;

}