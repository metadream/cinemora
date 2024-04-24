package com.arraywork.puffin.enums;

import com.arraywork.puffin.metafield.MetaFieldEnum;
import com.arraywork.springfield.databind.GenericEnum;
import com.arraywork.springfield.databind.GenericEnumConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审查枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Censorship implements MetaFieldEnum, GenericEnum<Integer> {

    UNCENSORED(0, "无码"),
    REDUCED(1, "消码"),
    CENSORED(2, "有码");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Censorship, Integer> {}

}