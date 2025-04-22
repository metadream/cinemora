package com.arraywork.cinemora.enums;

import com.arraywork.autumn.type.GenericEnum;
import com.arraywork.autumn.type.GenericEnumConverter;
import com.arraywork.cinemora.metafield.MetafieldEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 画质（分辨率）枚举
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Quality implements MetafieldEnum, GenericEnum<Integer> {

    UHD_8K(7680, "8K"),
    UHD_4K(3840, "4K"),
    FHD(1920, "FHD"),
    HD(1080, "HD"),
    SD(720, "SD"),
    LD(480, "LD");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Quality, Integer> { }

    /** 根据宽高获取画质（横竖屏通用） */
    public static Quality fromSize(int width, int height) {
        int max = Math.max(width, height);
        for (Quality q : Quality.values()) {
            if (max >= q.code) return q;
        }
        return null;
    }

}