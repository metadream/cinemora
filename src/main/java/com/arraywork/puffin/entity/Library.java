package com.arraywork.puffin.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.arraywork.puffin.basedata.Metafield;
import com.arraywork.springhood.LongIdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 媒体库
 * @author AiChen
 * @created 2024/04/22
 */
@Entity
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" }) // 序列化时忽略懒加载的属性
@DynamicInsert // 如果字段值为null则不会加入到insert语句中（此处的作用是为了使初始化空实体对象时产生带默认值的空数据行）
public class Library {

    @Id
    @Column(length = 20, insertable = false, updatable = false)
    @GenericGenerator(name = "long-id-generator", type = LongIdGenerator.class)
    @GeneratedValue(generator = "long-id-generator")
    private long id;

    // 媒体库路径
    @Column(updatable = false)
    @NotBlank(message = "媒体库路径不能为空")
    @Size(max = 100, message = "媒体库路径不能超过 {max} 个字符")
    private String path;

    // 媒体库名称
    @NotBlank(message = "媒体库名称不能为空")
    @Size(max = 10, message = "媒体库名称不能超过 {max} 个字符")
    private String name;

    // 元字段
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_ARRAY())")
    private Metafield[] metafields;

    // 是否重命名文件
    private boolean allowRenameFile;

    // 是否允许匿名访问
    private boolean allowAnonymous;

    // 创建时间
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

}