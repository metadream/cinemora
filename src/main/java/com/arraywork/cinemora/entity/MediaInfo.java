package com.arraywork.cinemora.entity;

import lombok.Data;

/**
 * 媒体信息
 *
 * @author Marco
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