package com.arraywork.puffin.basedata;

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
public enum Censorship {

    CENSORED("有码"),
    REDUCED("消码"),
    UNCENSORED("无码");

    private final String label;

}