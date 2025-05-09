package com.arraywork.cinemora.metafield;

import lombok.Data;

/**
 * 元字段实体
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
@Data
public class Metafield {

    private String name;
    private String label;
    private MetafieldEnum[] enums;

}