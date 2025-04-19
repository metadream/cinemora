package com.arraywork.cinemora.enums;

import com.arraywork.autumn.type.GenericEnum;
import com.arraywork.autumn.type.GenericEnumConverter;
import com.arraywork.cinemora.metafield.MetafieldEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 地区枚举
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Region implements MetafieldEnum, GenericEnum<Integer> {

    CHINESE(10, "中国"),
    JAPANESE(11, "日本"),
    KOREAN(12, "韩国"),
    ASIAN(20, "亚洲"),
    OCCIDENT(30, "欧美"),
    OTHERS(99, "其他");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Region, Integer> { }

}