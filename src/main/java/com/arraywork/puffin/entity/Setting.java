package com.arraywork.puffin.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.arraywork.springhood.LongIdGenerator;

import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 系统设置
 * @author AiChen
 * @created 2024/04/21
 */
@Data
@Entity
public class Setting {

    @Id
    @Column(length = 20, insertable = false, updatable = false)
    @GenericGenerator(name = "long-id-generator", type = LongIdGenerator.class)
    @GeneratedValue(generator = "long-id-generator")
    private long id;

    @Type(JsonStringType.class)
    private Administrator administrator;

    @Type(JsonStringType.class)
    private Preference preference;

}