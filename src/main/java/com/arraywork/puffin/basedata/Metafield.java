package com.arraywork.puffin.basedata;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 可选元字段
 * @author AiChen
 * @created 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Metafield {

    REGION("地区"),
    QUALITY("画质"),
    CENSORSHIP("审查"),
    RATING("分级"),
    PRODUCER("出品方"),
    DIRECTOR("导演"),
    STARRING("主演"),
    SERIES("系列"),
    GENRES("类型");

    private final String text;

}