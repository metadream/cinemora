package com.arraywork.puffin.enums;

import com.arraywork.puffin.metafield.MetaFieldEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审查枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Censorship implements MetaFieldEnum {

    CENSORED("有码"),
    REDUCED("消码"),
    UNCENSORED("无码");

    private final String label;

}