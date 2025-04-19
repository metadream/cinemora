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
public enum Quality implements MetafieldEnum, GenericEnum<String> {

    EK("8K", "8K"),
    FK("4K", "4K"),
    FHD("FHD", "FHD"),
    HD("HD", "HD"),
    SD("SD", "SD"),
    XX("XX", "");

    private final String code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Quality, String> { }

}