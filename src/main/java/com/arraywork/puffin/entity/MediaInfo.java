package com.arraywork.puffin.entity;

import lombok.Data;

/**
 * 媒体信息
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@Data
public class MediaInfo {

    private long duration;
    private String format;
    private VideoInfo video;
    private AudioInfo audio;

}