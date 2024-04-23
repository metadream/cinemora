package com.arraywork.puffin.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.arraywork.puffin.basedata.Metafield;
import com.arraywork.springhood.NanoIdGenerator;

import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 偏好实体
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@Entity
@Data
public class Preference {

    @Id
    @Column(length = 24, insertable = false, updatable = false)
    @GenericGenerator(name = "nano-id-generator", type = NanoIdGenerator.class)
    @GeneratedValue(generator = "nano-id-generator")
    private String id;

    // 用户名
    @Column(unique = true, updatable = false)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 20, message = "用户名不能超过 {max} 个字符")
    private String username;

    // 密码
    @NotBlank(message = "密码不能为空")
    @Size(max = 60, message = "密码不能超过 {max} 个字符")
    private String password;

    // 密码
    @Transient
    private String oldPassword;

    // 媒体库路径
    @NotBlank(message = "媒体库路径不能为空")
    @Size(max = 120, message = "媒体库路径不能超过 {max} 个字符")
    private String library;

    // 元字段
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_ARRAY())")
    private Metafield[] metafields;

    // 同步重命名文件
    private boolean syncRenameFile;

    // 最后更新时间
    @UpdateTimestamp
    private LocalDateTime lastModified;

}