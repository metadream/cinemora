package com.arraywork.puffin.service;

import org.springframework.stereotype.Service;

import com.arraywork.puffin.entity.MediaInfo;

/**
 * FFMPEG服务
 * @author AiChen
 * @created 2024/04/22
 */
@Service
public class FfmpegService {

    public MediaInfo getMediaInfo(String mediaPath) {
        return new MediaInfo();
    }

    public void screenshot(String videoPath, String outputPath, int duration) {}

}