package com.arraywork.cinemora.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件状态枚举
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/30
 */
@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventState {

    INDEXED("Indexed"),
    REINDEXED("Reindexed"),
    SKIPPED("Skipped"),
    DELETED("Deleted"),
    FAILED("Failed"),
    FINISHED("Finished");

    private final String label;

    // 序列化字面量属性
    public String getName() {
        return this.name();
    }

}