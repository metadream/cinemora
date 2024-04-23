package com.arraywork.puffin.basedata;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 元字段枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/23
 */
@AllArgsConstructor
@Getter
public enum Metafield {

    REGION("地区"),
    QUALITY("画质"),
    CENSORSHIP("审查"),
    RATING("分级"),
    PRODUCERS("制作方"),
    DIRECTORS("导演"),
    STARRING("主演"),
    GENRES("题材"),
    SERIES("系列"),
    TAGS("标签"),
    ISSUE_DATE("发行日期");

    private final String label;

}