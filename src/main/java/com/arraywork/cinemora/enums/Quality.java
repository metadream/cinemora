package com.arraywork.cinemora.enums;

import com.arraywork.autumn.type.GenericEnum;
import com.arraywork.autumn.type.GenericEnumConverter;
import com.arraywork.cinemora.metafield.MetafieldEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 画质枚举
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Quality implements MetafieldEnum, GenericEnum<Integer> {

    EK(7680, "8K"),
    FK(3840, "4K"),
    FHD(1080, "FHD"),
    HD(720, "HD"),
    SD(480, "SD"),
    LD(360, "LD");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Quality, Integer> { }

}