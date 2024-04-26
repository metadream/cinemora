package com.arraywork.puffin.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.arraywork.puffin.entity.AudioInfo;
import com.arraywork.puffin.entity.MediaInfo;
import com.arraywork.puffin.entity.VideoInfo;
import com.arraywork.springforce.util.Assert;

import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;

/**
 * FFMPEG服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class FfmpegService {

    // 获取媒体信息
    public MediaInfo getMediaInfo(File file) throws InputFormatException, EncoderException {
        Assert.isTrue(file != null && file.exists(), "File not exists: " + file);
        MultimediaInfo mInfo = new MultimediaObject(file).getInfo();
        ws.schild.jave.info.VideoInfo vInfo = mInfo.getVideo();
        Assert.isTrue(mInfo.getDuration() > 0 & vInfo != null, "File is not a video: " + file);

        // 获取视频信息
        VideoInfo video = new VideoInfo();
        video.setDecoder(vInfo.getDecoder());
        video.setBitRate(vInfo.getBitRate());
        video.setFrameRate(vInfo.getFrameRate());
        VideoSize vSize = vInfo.getSize();
        video.setWidth(vSize.getWidth());
        video.setHeight(vSize.getHeight());

        // 获取音频信息
        AudioInfo audio = new AudioInfo();
        ws.schild.jave.info.AudioInfo aInfo = mInfo.getAudio();
        audio.setDecoder(aInfo.getDecoder());
        audio.setChannels(aInfo.getChannels());
        audio.setBitRate(aInfo.getBitRate());
        audio.setSamplingRate(aInfo.getSamplingRate());

        // 获取媒体信息
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setDuration(mInfo.getDuration());
        mediaInfo.setFormat(mInfo.getFormat());
        mediaInfo.setAudio(audio);
        mediaInfo.setVideo(video);
        return mediaInfo;
    }

    // 视频截图
    public void screenshot(String videoPath, String outputPath, long time) {}

}