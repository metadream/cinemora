package com.arraywork.cinemora.entity;

import lombok.Data;

/**
 * 音频信息
 *
 * @author Marco
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