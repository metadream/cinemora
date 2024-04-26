package com.arraywork.puffin.entity;

import lombok.Data;

/**
 * 音频信息
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/21
 */
@Data
public class AudioInfo {

    private String decoder;
    private int channels;
    private int bitRate;
    private int samplingRate;

}