package com.arraywork.puffin.entity;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import com.arraywork.puffin.basedata.Censorship;
import com.arraywork.puffin.basedata.Quality;
import com.arraywork.puffin.basedata.Rating;
import com.arraywork.puffin.basedata.Region;
import com.arraywork.springhood.LongIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 元数据
 * @author AiChen
 * @created 2024/04/21
 */
@Entity
@Data
public class Metadata {

    @Id
    @Column(length = 20, insertable = false, updatable = false)
    @GenericGenerator(name = "long-id-generator", type = LongIdGenerator.class)
    @GeneratedValue(generator = "long-id-generator")
    private long id;

    // 编号
    @Column(unique = true)
    @NotBlank(message = "编号不能为空")
    @Size(max = 10, message = "编号不能超过{max}个字符")
    private String code;

    // 标题
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题不能超过 {max} 个字符")
    private String title;

    // 路径
    @NotBlank(message = "路径不能为空")
    @Size(max = 100, message = "路径不能超过 {max} 个字符")
    private String path;

    // 发行日期
    private LocalDate releaseDate;

    // private long videoSize;
    // private int videoWidth;
    // private int videoHeight;
    // private int duration;

    // 出品方
    @Size(max = 50, message = "出品方不能超过 {max} 个字符")
    private String producer;

    // 导演
    @Size(max = 50, message = "导演不能超过 {max} 个字符")
    private String director;

    // 地区
    private Region region;

    // 质量
    private Quality quality;

    // 审查
    private Censorship censorship;

    // 分级
    private Rating rating;

    // 题材
    @Size(max = 50, message = "题材不能超过 {max} 个字符")
    private String genres;

    // 系列
    @Size(max = 50, message = "系列不能超过 {max} 个字符")
    private String series;

    // 主演
    @Size(max = 50, message = "主演不能超过 {max} 个字符")
    private String[] starring;

    // 封面地址
    @Size(max = 50, message = "封面地址不能超过 {max} 个字符")
    private String coverUrl;

    // 是否标星
    private boolean starred;

}