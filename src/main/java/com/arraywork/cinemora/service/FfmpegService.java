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
 * FFMPEG 服务
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
@Slf4j
public class FfmpegService {

    private static final Map<String, ProcessWrapper> ffmpegProcesses = new HashMap<>();

    /** 提取媒体信息 */
    public MediaInfo extract(File file) {
        MediaInfo mediaInfo = null;
        try {
            MultimediaInfo mInfo = new MultimediaObject(file).getInfo();

            // 获取通用信息
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

                // 获取视频信息（有些图片也会被提取为视频，所以增加BitRate和FrameRate校验）
                // 某些图片也会被提取出视频元数据，bitRate=-1, frameRate通常为25, duration可能是40
                // 某些视频如wmv提取元数据后，bitRate也可能为-1, frameRate>0, duration>0
                // 某些视频如avi提起元数据后，decoder是mjpeg，但bitRate==-1
                // 但无法得知是否还有其他未验证的情况
                ws.schild.jave.info.VideoInfo vInfo = mInfo.getVideo();
                System.out.println(vInfo);
                if (vInfo != null && vInfo.getFrameRate() > 0 && mediaInfo.getDuration() > 0) {
                    String decoder = vInfo.getDecoder().replaceAll(" \\(.+", ""); // 去掉空格及后面的括号

                    if (!("mjpeg".equalsIgnoreCase(decoder) && vInfo.getBitRate() == -1)) {
                        VideoInfo video = new VideoInfo();
                        video.setDecoder(decoder);
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
            }
        } catch (EncoderException e) {
            mediaInfo = null;
        }
        return mediaInfo;
    }

    /** 视频截图（指定毫秒时长所在帧） */
    public void screenshot(File videoFile, File outputFile, long millis) {
        MultimediaObject mObject = new MultimediaObject(videoFile);
        ScreenExtractor screenExtractor = new ScreenExtractor();
        try {
            screenExtractor.renderOneImage(mObject, -1, -1, millis, outputFile, 1);
        } catch (EncoderException e) {
            log.error("Screenshot error: ", e);
        }
    }

    /** 视频转码输出流 */
    @Deprecated
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

    /** 终止转码进程 */
    public void destroy(String transId) {
        ProcessWrapper ffmpeg = ffmpegProcesses.get(transId);
        if (ffmpeg != null) ffmpeg.destroy();
    }

}