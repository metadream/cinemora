package com.arraywork.puffin.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.arraywork.springhood.LongIdGenerator;

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
public class MediaLibrary {

    @Id
    @Column(length = 20, insertable = false, updatable = false)
    @GenericGenerator(name = "long-id-generator", type = LongIdGenerator.class)
    @GeneratedValue(generator = "long-id-generator")
    private long id;

    // 媒体库路径
    @Column(updatable = false)
    @NotBlank(message = "媒体库路径不能为空")
    @Size(max = 100, message = "媒体库路径长度不能超过{max}")
    private String path;

    // 媒体库名称
    @NotBlank(message = "媒体库名称不能为空")
    @Size(max = 10, message = "媒体库名称长度不能超过{max}")
    private String name;

    // 可选的元数据字段
    // @Type(JsonStringType.class)
    private String[] metafields;

    // 是否重命名文件
    private boolean allowRenameFile;

    // 是否允许匿名访问
    private boolean allowAnonymous;

    // 创建时间
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

}