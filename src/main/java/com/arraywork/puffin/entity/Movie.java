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
import lombok.Data;

/**
 * 电影信息
 * @author AiChen
 * @created 2024/04/21
 */
@Data
@Entity
public class Movie {

    @Id
    @Column(length = 20, insertable = false, updatable = false)
    @GenericGenerator(name = "long-id-generator", type = LongIdGenerator.class)
    @GeneratedValue(generator = "long-id-generator")
    private long id;

    private String code;
    private String title;
    private String path;
    private LocalDate releaseDate;

    private long videoSize;
    private int videoWidth;
    private int videoHeight;
    private int duration;

    private String producer;
    private String director;
    private Region region;
    private Quality quality;
    private Censorship censorship;
    private Rating rating;
    private String genres;
    private String series;
    private String starring;
    private String coverImageData;
    private boolean starred;

}