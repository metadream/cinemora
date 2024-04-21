package com.arraywork.puffin.entity;

import lombok.Data;

/**
 * 偏好设置
 * @author AiChen
 * @created 2024/04/19
 */
@Data
public class Preference {

    private String library;
    private String[] metafields;
    private boolean codify;

}