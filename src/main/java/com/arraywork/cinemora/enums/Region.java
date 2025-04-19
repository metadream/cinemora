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

    CHINA(10, "China"),
    JAPAN(11, "Japan"),
    KOREA(12, "Korea"),
    ASIA(20, "Asia"),
    OCCIDENT(30, "Occident"),
    OTHERS(99, "Others");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Region, Integer> { }

}