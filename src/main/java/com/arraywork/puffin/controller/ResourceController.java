package com.arraywork.puffin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.service.FfmpegService;
import com.arraywork.puffin.service.MetadataService;
import com.arraywork.springforce.StaticResourceHandler;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ws.schild.jave.process.ProcessWrapper;

/**
 * 资源控制器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/28
 */
@Controller
public class ResourceController {

    @Resource
    private StaticResourceHandler resourceHandler;
    @Resource
    private FfmpegService ffmpegService;
    @Resource
    private MetadataService metadataService;

    @Value("${puffin.cover.base-dir}")
    private String coverBaseDir;

    // 封面资源
    @GetMapping("/cover/{id}")
    public void cover(HttpServletRequest request, HttpServletResponse response,
        @PathVariable String id) throws IOException {
        Path coverPath = Path.of(coverBaseDir, id + ".jpg");
        resourceHandler.serve(coverPath, request, response);
    }

    // 视频资源
    @GetMapping("/video/{id}")
    public void video(HttpServletRequest request, HttpServletResponse response,
        @PathVariable String id) throws IOException {
        Metadata metadata = metadataService.getById(id);
        Path videoPath = Path.of(metadata.getFilePath());
        resourceHandler.serve(videoPath, request, response);
    }

    // 视频转码
    @GetMapping("/transcode/{id}")
    public void transcode(HttpServletRequest request, HttpServletResponse response,
        @PathVariable String id) throws IOException {
        response.setContentType("video/mp4");
        Metadata metadata = metadataService.getById(id);
        ProcessWrapper ffmpeg = null;
        InputStream input = null;
        OutputStream output = null;

        try {
            ffmpeg = ffmpegService.transcode(metadata.getFilePath());
            input = ffmpeg.getInputStream();
            output = response.getOutputStream();
            resourceHandler.copy(input, output);
        } finally {
            resourceHandler.close(input);
            resourceHandler.close(output);
            if (ffmpeg != null) ffmpeg.destroy();
        }
    }

}