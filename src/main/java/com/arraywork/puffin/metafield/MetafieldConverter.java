package com.arraywork.puffin.metafield;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.arraywork.springfield.util.JsonUtils;
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
    private MetafieldManager metafieldManager;

    // 实现JPA实体到数据库的转换
    @Override
    public String convertToDatabaseColumn(List<Metafield> attributes) {
        return JsonUtils.stringify(attributes.stream().map(v -> v.getName()).toArray());
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
        List<Metafield> metafields = metafieldManager.getMetafields();

        if (values != null) {
            for (String value : values) {
                Optional<Metafield> optional = metafields.stream()
                    .filter(v -> v.getName().equals(value)).findAny();
                optional.ifPresent(v -> result.add(v));
            }
        }
        return result;
    }

}