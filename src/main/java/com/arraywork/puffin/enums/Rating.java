package com.arraywork.puffin.enums;

import com.arraywork.puffin.metafield.MetaFieldEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分级枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Rating implements MetaFieldEnum {

    UNIVERSAL("普适级"),
    GUIDANCE("指导级"),
    RESTRICTED("限制级"),
    ADULT("成人级");

    private final String label;

}