package com.arraywork.puffin.entity;

import java.time.LocalDate;

import com.arraywork.puffin.basedata.Censorship;
import com.arraywork.puffin.basedata.Quality;
import com.arraywork.puffin.basedata.Rating;
import com.arraywork.puffin.basedata.Region;

import lombok.Data;

/**
 * 电影信息
 * @author AiChen
 * @created 2024/04/21
 */
@Data
public class Movie {

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