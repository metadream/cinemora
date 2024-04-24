package com.arraywork.puffin.metafield;

import lombok.Data;

/**
 * 元字段信息
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
@Data
public class Metafield {

    private String name;
    private String label;
    private MetaFieldEnum[] enums;

}