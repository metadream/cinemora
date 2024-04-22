package com.arraywork.puffin.basedata;

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
public enum Quality {

    FK("4K"),
    UHD("超高清"),
    FHD("全高清"),
    HD("高清"),
    SD("标清"),
    ETC("其他");

    private final String label;

}