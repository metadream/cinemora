package com.arraywork.puffin.enums;

import com.arraywork.puffin.metafield.MetafieldEnum;
import com.arraywork.springforce.databind.GenericEnum;
import com.arraywork.springforce.databind.GenericEnumConverter;

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
public enum Censorship implements MetafieldEnum, GenericEnum<Integer> {

    UNCENSORED(0, "无码"),
    REDUCED(1, "消码"),
    CENSORED(2, "有码");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Censorship, Integer> {}

}