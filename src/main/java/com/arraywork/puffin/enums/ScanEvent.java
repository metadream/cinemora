package com.arraywork.puffin.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScanEvent {

    SCAN("扫描"),
    ADD("新增"),
    MODIFY("更新"),
    DELETE("删除"),
    PURGE("清除");

    private final String label;

    // 序列化字面量属性
    public String getName() {
        return this.name();
    }

}