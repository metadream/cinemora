package com.arraywork.puffin.basedata;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审查
 * @author AiChen
 * @created 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Censorship {

    CENSORED("有码"),
    REDUCED("消码"),
    UNCENSORED("无码");

    private final String text;

}