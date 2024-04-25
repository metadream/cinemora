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

    CN("cn", "中国"),
    JP("jp", "日本"),
    KR("kr", "韩国"),
    IN("in", "印度"),
    TH("th", "泰国"),
    US("us", "美国"),
    CA("ca", "加拿大"),
    RU("ru", "俄罗斯"),
    GB("gb", "英国"),
    FR("fr", "法国"),
    DE("de", "德国"),
    IT("it", "意大利"),
    ES("es", "西班牙"),
    IE("ie", "爱尔兰"),
    SE("se", "瑞典"),
    DK("dk", "丹麦"),
    AU("au", "澳大利亚"),
    BR("br", "巴西"),
    XX("xx", "其他");

    private final String code;
    private final String label;

    public static class Converter extends GenericEnumConverter<Region, String> {}

}