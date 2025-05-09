package com.arraywork.cinemora.enums;

import com.arraywork.autumn.type.GenericEnum;
import com.arraywork.autumn.type.GenericEnumConverter;
import com.arraywork.cinemora.metafield.MetafieldEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分级枚举
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Rating implements MetafieldEnum, GenericEnum<Integer> {

    UNIVERSAL(0, "Universal"),      // 普适级
    GUIDANCE(1, "Guidance"),        // 指导级
    RESTRICTED(2, "Restricted"),    // 限制级
    ADULT(3, "Adult");              // 成人级

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Rating, Integer> { }

}