package com.arraywork.puffin.enums;

import com.arraywork.puffin.metafield.MetaFieldEnum;
import com.arraywork.springfield.databind.GenericEnum;
import com.arraywork.springfield.databind.GenericEnumConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分级枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Rating implements MetaFieldEnum, GenericEnum<Integer> {

    UNIVERSAL(0, "普适级"),
    GUIDANCE(1, "指导级"),
    RESTRICTED(2, "限制级"),
    ADULT(3, "成人级");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Rating, Integer> {}

}