package com.arraywork.puffin.metafield;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.arraywork.puffin.entity.Metadata;

/**
 * 元字段管理器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
public class MetafieldManager {

    public static List<Metafield> metafields;

    // 获取所有元字段
    public static List<Metafield> getMetafields() {
        if (metafields != null) return metafields;
        metafields = new ArrayList<>();
        Field[] fields = Metadata.class.getDeclaredFields();

        for (Field field : fields) {
            MetaColumn annotation = field.getAnnotation(MetaColumn.class);
            if (annotation == null) continue;

            Metafield metafield = new Metafield();
            metafield.setName(field.getName());
            metafield.setLabel(annotation.label());

            if (field.getType().isEnum()) {
                MetafieldEnum[] enums = (MetafieldEnum[]) field.getType().getEnumConstants();
                metafield.setEnums(enums);
            }
            metafields.add(metafield);
        }
        return metafields;
    }

}