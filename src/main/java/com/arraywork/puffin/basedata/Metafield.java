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

    RATING("分级");

    private final String label;

}