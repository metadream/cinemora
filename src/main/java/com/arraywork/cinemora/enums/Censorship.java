package com.arraywork.cinemora.enums;

import com.arraywork.autumn.type.GenericEnum;
import com.arraywork.autumn.type.GenericEnumConverter;
import com.arraywork.cinemora.metafield.MetafieldEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审查枚举
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Censorship implements MetafieldEnum, GenericEnum<Integer> {

    NEUTRAL(0, "Neutral"),
    UNCENSORED(1, "Uncensored"),
    REDUCED(2, "Reduced"),
    CENSORED(3, "Censored");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Censorship, Integer> { }

}