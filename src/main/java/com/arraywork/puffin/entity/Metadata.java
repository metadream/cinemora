package com.arraywork.puffin.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.arraywork.puffin.enums.Censorship;
import com.arraywork.puffin.enums.Quality;
import com.arraywork.puffin.enums.Rating;
import com.arraywork.puffin.enums.Region;
import com.arraywork.puffin.metafield.MetaColumn;
import com.arraywork.springforce.util.KeyGenerator;
import com.arraywork.springforce.util.Validator;

import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 元数据
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@Entity
@DynamicInsert
@Data
public class Metadata {

    @Id
    @Column(length = 24, insertable = false, updatable = false)
    @GenericGenerator(name = "nano-id-generator", type = KeyGenerator.NanoId.class)
    @GeneratedValue(generator = "nano-id-generator")
    private String id;

    // 编号
    @Column(unique = true)
    @NotBlank(message = "编号不能为空", groups = Validator.Update.class)
    @Size(max = 20, message = "编号不能超过{max}个字符")
    private String code;

    // 标题
    @NotBlank(message = "标题不能为空")
    @Size(max = 120, message = "标题不能超过 {max} 个字符")
    private String title;

    // 发行日
    @MetaColumn(label = "发行日")
    private LocalDate issueDate;

    // 制作方
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_ARRAY())")
    @MetaColumn(label = "制作方")
    private String[] producers;

    // 导演
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_ARRAY())")
    @MetaColumn(label = "导演")
    private String[] directors;

    // 主演
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_ARRAY())")
    @MetaColumn(label = "主演")
    private String[] starring;

    // 系列
    @Size(max = 60, message = "系列名不能超过 {max} 个字符")
    @MetaColumn(label = "系列")
    private String series;

    // 题材
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_ARRAY())")
    @MetaColumn(label = "题材")
    private String[] genres;

    // 标签
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_ARRAY())")
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

    // 媒体信息
    @Type(JsonStringType.class)
    @Column(columnDefinition = "JSON DEFAULT (JSON_OBJECT())")
    private MediaInfo mediaInfo;

    // 文件路径
    @Column(unique = true)
    @NotBlank(message = "路径不能为空")
    @Size(max = 120, message = "路径不能超过 {max} 个字符")
    private String filePath;

    // 文件大小
    private long fileSize;

    // 封面地址
    @Size(max = 60, message = "封面地址不能超过 {max} 个字符")
    private String coverUrl;

    // 是否标星
    private boolean starred;

    // 更新时间
    @UpdateTimestamp
    private LocalDateTime lastModified;

    // 关键词搜索条件
    @Transient
    private String keyword;

}