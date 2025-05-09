package com.arraywork.cinemora.entity;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import com.arraywork.autumn.security.Principal;
import com.arraywork.autumn.security.SecurityRole;
import com.arraywork.autumn.util.Validator;
import com.arraywork.cinemora.metafield.Metafield;
import com.arraywork.cinemora.metafield.MetafieldConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统设置（继承安全用户）
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@Entity
@DynamicInsert
@Data
@EqualsAndHashCode(callSuper = false)
public class Settings extends Principal {

    // 一个系统只有一条设置记录
    @Id
    private long settingId = Long.MAX_VALUE;

    // 用户名
    @Column(unique = true)
    @NotBlank(message = "The username cannot be empty")
    @Size(max = 20, message = "The username length cannot exceed {max} characters")
    private String username;

    // 密码
    @NotBlank(message = "The password cannot be empty", groups = Validator.Insert.class)
    @Size(max = 60, message = "The password length cannot exceed {max} characters")
    private String password;

    // 媒体库路径
    @NotBlank(message = "The library cannot be empty")
    @Size(max = 120, message = "The library length cannot exceed {max} characters")
    private String library;

    // 元字段
    @JsonDeserialize(converter = MetafieldConverter.class)
    @Convert(converter = MetafieldConverter.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_ARRAY())")
    private List<Metafield> metafields;

    // 自动重命名文件
    private boolean autoRename;

    // 最后更新时间
    @UpdateTimestamp
    private LocalDateTime lastModified;

    // 不使用角色控制
    @Override
    public List<SecurityRole> getSecurityRoles() {
        return null;
    }

}