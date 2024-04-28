package com.arraywork.puffin.metafield;

import java.util.ArrayList;
import java.util.List;

import com.arraywork.springforce.util.Arrays;
import com.arraywork.springforce.util.Jackson;
import com.fasterxml.jackson.databind.util.StdConverter;

import jakarta.annotation.Resource;
import jakarta.persistence.AttributeConverter;

/**
 * 元字段转换器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
public class MetafieldConverter extends StdConverter<String[], List<Metafield>>
    implements AttributeConverter<List<Metafield>, String> {

    @Resource
    private Jackson jackson;

    // 实现JPA实体到数据库的转换
    @Override
    public String convertToDatabaseColumn(List<Metafield> attributes) {
        if (attributes != null) {
            return jackson.stringify(Arrays.map(attributes, Metafield::getName));
        }
        return "[]";
    }

    // 实现JPA数据库到实体的转换
    @Override
    public List<Metafield> convertToEntityAttribute(String dbData) {
        return convert(jackson.parse(dbData, String[].class));
    }

    // 实现StdConverter反序列化方法
    @Override
    public List<Metafield> convert(String[] values) {
        List<Metafield> result = new ArrayList<>();
        List<Metafield> metafields = MetafieldManager.getMetafields();

        if (values != null) {
            for (String value : values) {
                Metafield metafield = Arrays.findAny(metafields, v -> v.getName().equals(value));
                if (metafield != null) result.add(metafield);
            }
        }
        return result;
    }

}