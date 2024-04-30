package com.arraywork.puffin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扫描事件枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/30
 */
@AllArgsConstructor
@Getter
public enum ScanEvent {

    SCAN("扫描"),
    ADD("新增"),
    MODIFY("更新"),
    DELETE("删除");

    private final String label;

}