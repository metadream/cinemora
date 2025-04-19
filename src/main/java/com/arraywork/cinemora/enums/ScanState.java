package com.arraywork.cinemora.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扫描状态枚举
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/30
 */
@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScanState {

    SUCCESS("成功"),
    SKIPPED("跳过"),
    FAILED("失败"),
    FINISHED("结束");

    private final String label;

    // 序列化字面量属性
    public String getName() {
        return this.name();
    }

}