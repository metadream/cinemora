package com.arraywork.puffin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扫描状态枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/30
 */
@AllArgsConstructor
@Getter
public enum ScanState {

    SUCCESS("成功"),
    SKIPPED("跳过"),
    FAILED("失败"),
    FINISHED("结束");

    private final String label;

}