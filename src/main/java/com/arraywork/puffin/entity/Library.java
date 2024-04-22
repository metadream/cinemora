package com.arraywork.puffin.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.arraywork.springhood.NanoIdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 媒体库实体
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Entity
@Data
public class Library {

    @Id
    @Column(length = 24, insertable = false, updatable = false)
    @GenericGenerator(name = "nano-id-generator", type = NanoIdGenerator.class)
    @GeneratedValue(generator = "nano-id-generator")
    private String id;

    // 媒体库名称
    @NotBlank(message = "媒体库名称不能为空")
    @Size(max = 20, message = "媒体库名称不能超过 {max} 个字符")
    private String name;

    // 媒体库路径
    @Column(updatable = false)
    @NotBlank(message = "媒体库路径不能为空")
    @Size(max = 100, message = "媒体库路径不能超过 {max} 个字符")
    private String path;

    // 是否重命名文件
    private boolean allowRenameFile;

    // 是否允许匿名访问
    private boolean allowAnonymous;

    // 创建时间
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

    //// 元数据
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "library_id")
    @OrderBy("lastModified DESC")
    private List<Metadata> metadatas;

}