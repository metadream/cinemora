package com.arraywork.puffin.enums;

import com.arraywork.puffin.metafield.MetafieldEnum;
import com.arraywork.springforce.databind.GenericEnum;
import com.arraywork.springforce.databind.GenericEnumConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 画质枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Quality implements MetafieldEnum, GenericEnum<String> {

    EK("8K", "8K"),
    FK("4K", "4K"),
    FHD("FHD", "全高清"),
    HD("HD", "高清"),
    SD("SD", "标清"),
    XX("XX", "其他");

    private final String code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Quality, String> {}

}