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
 * @author Marco
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
    @NotBlank(message = "The code cannot be empty")
    @Size(max = 20, message = "The code length cannot exceed {max} characters")
    private String code;

    // 标题
    @NotBlank(message = "The title cannot be empty")
    @Size(max = 255, message = "The title length cannot exceed {max} characters")
    private String title;

    // 制作方
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "Producers")
    private String[] producers;

    // 导演
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "Directors")
    private String[] directors;

    // 主演
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "Starring")
    private String[] starring;

    // 系列
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "Series")
    private String[] series;

    // 题材
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "Genres")
    private String[] genres;

    // 标签
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT NULL")
    @MetaColumn(label = "Tags")
    private String[] tags;

    // 地区
    @Convert(converter = Region.Converter.class)
    @MetaColumn(label = "Region")
    private Region region;

    // 画质
    @Convert(converter = Quality.Converter.class)
    @MetaColumn(label = "Quality")
    private Quality quality;

    // 审查
    @Convert(converter = Censorship.Converter.class)
    @MetaColumn(label = "Censorship")
    private Censorship censorship;

    // 分级
    @Convert(converter = Rating.Converter.class)
    @MetaColumn(label = "Rating")
    private Rating rating;

    // 发行日
    @MetaColumn(label = "Issue Date")
    private LocalDate issueDate;

    // 媒体信息
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_OBJECT())")
    private MediaInfo mediaInfo;

    // 文件路径
    @Column(unique = true)
    @NotBlank(message = "File path cannot be empty")
    private String filePath;

    // 文件大小
    private long fileSize;

    // 文件创建时间
    private LocalDateTime fileTime;

    // 是否标星
    private boolean starred;

    // 是否更新过
    private boolean updated;

    // 搜索关键词
    @Transient
    private String keyword;

}