package com.arraywork.puffin.service;

import org.springframework.stereotype.Service;

import com.arraywork.puffin.entity.MediaInfo;

/**
 * FFMPEG服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class FfmpegService {

    // 获取媒体信息
    public MediaInfo getMediaInfo(String mediaPath) {
        return new MediaInfo();
    }

    // 视频截图
    public void screenshot(String videoPath, String outputPath, int duration) {}

}