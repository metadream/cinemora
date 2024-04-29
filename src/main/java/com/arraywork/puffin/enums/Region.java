package com.arraywork.puffin.enums;

import com.arraywork.puffin.metafield.MetafieldEnum;
import com.arraywork.springforce.databind.GenericEnum;
import com.arraywork.springforce.databind.GenericEnumConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 地区枚举
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Region implements MetafieldEnum, GenericEnum<String> {

    CN("CN", "中国"),
    JP("JP", "日本"),
    KR("KR", "韩国"),
    IN("IN", "印度"),
    TH("TH", "泰国"),
    US("US", "美国"),
    CA("CA", "加拿大"),
    RU("RU", "俄罗斯"),
    GB("GB", "英国"),
    FR("FR", "法国"),
    DE("DE", "德国"),
    IT("IT", "意大利"),
    ES("ES", "西班牙"),
    IE("IE", "爱尔兰"),
    SE("SE", "瑞典"),
    DK("DK", "丹麦"),
    AU("AU", "澳大利亚"),
    BR("BR", "巴西"),
    XX("XX", "其他");

    private final String code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Region, String> {}

}