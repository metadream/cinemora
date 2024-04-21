package com.arraywork.puffin.entity;

import lombok.Data;

/**
 * 媒体信息
 * @author AiChen
 * @created 2024/04/21
 */
@Data
public class MediaInfo {

    private long size;
    private long duration;
    private int bitRate;
    private VideoInfo videoInfo;
    private AudioInfo audioInfo;

}