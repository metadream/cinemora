package com.arraywork.cinemora.entity;

import lombok.Data;

/**
 * 视频信息
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@Data
public class VideoInfo {

    private String decoder;
    private int width;
    private int height;
    private int bitRate;
    private float frameRate;

}