package com.arraywork.cinemora.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import com.arraywork.autumn.id.NanoIdGeneration;
import com.arraywork.cinemora.enums.Censorship;
import com.arraywork.cinemora.enums.Quality;
import com.arraywork.cinemora.enums.Rating;
import com.arraywork.cinemora.enums.Region;
import com.arraywork.cinemora.metafield.MetaColumn;

import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import lombok.Data;

/**
 * 元数据
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@Entity
@DynamicInsert
@Data
public class Metadata {

    @Id
    @NanoIdGeneration
    @Column(length = 24, insertable = false, updatable = false)
    private String id;

    // 编号
    @Column(unique = true)
    @NotBlank(message = "编号不能为空")
    @Size(max = 20, message = "编号不能超过{max}个字符")
    private String code;

    // 标题
    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题不能超过 {max} 个字符")
    private String title;

    // 制作方
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "制作方")
    private String[] producers;

    // 导演
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "导演")
    private String[] directors;

    // 主演
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "主演")
    private String[] starring;

    // 系列
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "系列")
    private String[] series;

    // 题材
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "题材")
    private String[] genres;

    // 标签
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "标签")
    private String[] tags;

    // 地区
    @Convert(converter = Region.Converter.class)
    @MetaColumn(label = "地区")
    private Region region;

    // 画质
    @Convert(converter = Quality.Converter.class)
    @MetaColumn(label = "画质")
    private Quality quality;

    // 审查
    @Convert(converter = Censorship.Converter.class)
    @MetaColumn(label = "审查")
    private Censorship censorship;

    // 分级
    @Convert(converter = Rating.Converter.class)
    @MetaColumn(label = "分级")
    private Rating rating;

    // 发行日
    @MetaColumn(label = "发行日")
    private LocalDate issueDate;

    // 媒体信息
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_OBJECT())")
    private MediaInfo mediaInfo;

    // 文件路径
    @Column(unique = true)
    @NotBlank(message = "路径不能为空")
    private String filePath;

    // 文件大小
    private long fileSize;

    // 文件更新时间
    private LocalDateTime lastModified;

    // 是否标星
    private boolean starred;

    // 关键词搜索条件
    @Transient
    private String keyword;

}