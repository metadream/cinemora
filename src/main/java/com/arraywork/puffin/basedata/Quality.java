package com.arraywork.puffin.basedata;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 视频质量
 * @author AiChen
 * @created 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Quality {

    FK("4K"),
    UHD("超高清"),
    FHD("全高清"),
    HD("高清"),
    SD("标清"),
    OT("其他");

    private final String text;

}