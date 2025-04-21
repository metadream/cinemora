package com.arraywork.cinemora.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件源枚举
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/30
 */
@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventSource {

    SCANNING("Scanning..."),
    LISTENING("Listening..."),
    CLEANING("Cleaning...");

    private final String label;

    // 序列化字面量属性
    public String getName() {
        return this.name();
    }

}