package com.arraywork.cinemora.metafield;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.AttributeConverter;

import com.arraywork.autumn.util.JsonUtils;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * 元字段转换器
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
public class MetafieldConverter extends StdConverter<String[], List<Metafield>>
    implements AttributeConverter<List<Metafield>, String> {

    // 实现JPA实体到数据库的转换
    @Override
    public String convertToDatabaseColumn(List<Metafield> attributes) {
        if (attributes != null) {
            return JsonUtils.stringify(attributes.stream().map(Metafield::getName).toList());
        }
        return "[]";
    }

    // 实现JPA数据库到实体的转换
    @Override
    public List<Metafield> convertToEntityAttribute(String dbData) {
        return convert(JsonUtils.parse(dbData, String[].class));
    }

    // 实现StdConverter反序列化方法
    @Override
    public List<Metafield> convert(String[] values) {
        List<Metafield> result = new ArrayList<>();
        List<Metafield> metafields = MetafieldManager.getMetafields();

        if (values != null) {
            for (String value : values) {
                Metafield metafield = metafields.stream().filter(v -> v.getName().equals(value)).findAny().orElse(null);
                if (metafield != null) result.add(metafield);
            }
        }
        return result;
    }

}