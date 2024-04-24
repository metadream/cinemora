package com.arraywork.puffin.metafield;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.entity.Metafield;

/**
 * 元字段管理器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
@Service
public class MetafieldManager {

    public static List<Metafield> metafields;

    // 获取所有元字段
    public List<Metafield> getMetafields() {
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
                MetaFieldEnum[] values = (MetaFieldEnum[]) field.getType().getEnumConstants();
                metafield.setValues(values);
            }
            metafields.add(metafield);
        }
        return metafields;
    }

}