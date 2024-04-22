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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户
 * @author AiChen
 * @created 2024/04/21
 */
@Entity
@Data
public class User {

    @Id
    @Column(length = 20, insertable = false, updatable = false)
    @GenericGenerator(name = "long-id-generator", type = LongIdGenerator.class)
    @GeneratedValue(generator = "long-id-generator")
    private long id;

    // 用户名
    @Column(unique = true, updatable = false)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名必须在 {min}-{max} 个字符之间")
    @Pattern(regexp = "[a-z0-9_\\-\\.]{4,}", message = "用户名只能包含小写字母、数字、下划线、短线、点号")
    private String username;

    // 密码
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 60, message = "密码必须在 {min}-{max} 个字符之间")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*", message = "密码必须同时包含大小写字母和数字")
    private String password;

    // 媒体库访问权限
    private String[] permissions;

    // 是否超级用户
    private boolean isSuper;

    // 创建时间
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

}