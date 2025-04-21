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
    FHD(1080, "FHD"),
    HD(720, "HD"),
    SD(480, "SD"),
    LD(360, "LD");

    private final Integer code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Quality, Integer> { }

    public static Quality fromSize(int width, int height) {
        int max = Math.max(width, height); // 横竖屏通用
        for (Quality r : Quality.values()) {
            if (max >= r.code) return r;
        }
        return null;
    }

    public static void main(String[] args) {
        Quality r1 = Quality.fromSize(370, 480);
        System.out.println(r1);
    }

}