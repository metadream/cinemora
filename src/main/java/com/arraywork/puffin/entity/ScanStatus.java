package com.arraywork.puffin.entity;

import lombok.ToString;

/**
 * 扫描状态
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@ToString
public class ScanStatus {

    public int count;
    public int total;
    public int success;
    public int failed;
    public String path;
    public String event;
    public String result;
    public String error;

}