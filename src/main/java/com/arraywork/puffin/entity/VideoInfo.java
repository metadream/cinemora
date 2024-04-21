package com.arraywork.puffin.entity;

import lombok.Data;

/**
 * 视频信息
 * @author AiChen
 * @created 2024/04/21
 */
@Data
public class VideoInfo {

    private String codec;
    private String aspectRatio;
    private int width;
    private int height;
    private int frameRate;
    private int bitRate;

}