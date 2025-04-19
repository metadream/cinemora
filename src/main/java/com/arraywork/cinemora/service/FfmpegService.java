package com.arraywork.cinemora.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.arraywork.cinemora.entity.AudioInfo;
import com.arraywork.cinemora.entity.MediaInfo;
import com.arraywork.cinemora.entity.VideoInfo;

import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.ScreenExtractor;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

/**
 * FFMPEG服务
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
@Slf4j
public class FfmpegService {

    private static final Map<String, ProcessWrapper> ffmpegProcesses = new HashMap<>();

    // 获取媒体信息
    public MediaInfo extract(File file) {
        MediaInfo mediaInfo = null;
        try {
            MultimediaInfo mInfo = new MultimediaObject(file).getInfo();

            // 获取媒体信息
            if (mInfo.getFormat() != null && mInfo.getDuration() > 0) {
                mediaInfo = new MediaInfo();
                mediaInfo.setDuration(mInfo.getDuration());
                mediaInfo.setFormat(mInfo.getFormat());

                // 获取音频信息
                ws.schild.jave.info.AudioInfo aInfo = mInfo.getAudio();
                if (aInfo != null) {
                    AudioInfo audio = new AudioInfo();
                    audio.setDecoder(aInfo.getDecoder().replaceAll(" \\(.+", ""));
                    audio.setChannels(aInfo.getChannels());
                    audio.setBitRate(aInfo.getBitRate());
                    audio.setSamplingRate(aInfo.getSamplingRate());
                    mediaInfo.setAudio(audio);
                }

                // 获取视频信息
                ws.schild.jave.info.VideoInfo vInfo = mInfo.getVideo();
                if (vInfo != null) {
                    VideoInfo video = new VideoInfo();
                    video.setDecoder(vInfo.getDecoder().replaceAll(" \\(.+", ""));
                    video.setBitRate(vInfo.getBitRate());
                    video.setFrameRate(vInfo.getFrameRate());

                    // 视频尺寸
                    VideoSize vSize = vInfo.getSize();
                    if (vSize != null) {
                        video.setWidth(vSize.getWidth());
                        video.setHeight(vSize.getHeight());
                    }
                    mediaInfo.setVideo(video);
                }
            }
        } catch (EncoderException e) {
            mediaInfo = null;
        }
        return mediaInfo;
    }

    // 视频截图
    public void screenshot(File videoFile, File outputFile, long millis) {
        MultimediaObject mObject = new MultimediaObject(videoFile);
        ScreenExtractor screenExtractor = new ScreenExtractor();
        try {
            screenExtractor.renderOneImage(mObject, -1, -1, millis, outputFile, 1);
        } catch (EncoderException e) {
            e.printStackTrace();
            log.error("Screenshot error: ", e);
        }
    }

    // 视频转码
    public InputStream transcode(String transId, String videoFile) throws IOException {
        ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoFile);
        ffmpeg.addArgument("-preset:v");
        ffmpeg.addArgument("ultrafast");
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("mp4");
        ffmpeg.addArgument("-movflags");
        ffmpeg.addArgument("frag_keyframe+empty_moov");
        ffmpeg.addArgument("-");
        ffmpeg.execute();

        ffmpegProcesses.put(transId, ffmpeg);
        return ffmpeg.getInputStream();
    }

    // 终止转码进程
    public void destroy(String transId) {
        ProcessWrapper ffmpeg = ffmpegProcesses.get(transId);
        if (ffmpeg != null) ffmpeg.destroy();
    }

}