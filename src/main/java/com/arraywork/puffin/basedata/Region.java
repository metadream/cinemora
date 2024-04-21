package com.arraywork.puffin.basedata;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 地区
 * @author AiChen
 * @created 2024/04/21
 */
@AllArgsConstructor
@Getter
public enum Region {

    CN("中国"),
    JP("日本"),
    KR("韩国"),
    IN("印度"),
    TH("泰国"),
    US("美国"),
    CA("加拿大"),
    RU("俄罗斯"),
    GB("英国"),
    FR("法国"),
    DE("德国"),
    IT("意大利"),
    ES("西班牙"),
    IE("爱尔兰"),
    SE("瑞典"),
    DK("丹麦"),
    AU("澳大利亚"),
    BR("巴西"),
    AS("亚洲"),
    OC("欧美"),
    OT("其他");

    private final String text;

}