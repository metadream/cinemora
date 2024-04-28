package com.arraywork.puffin.controller;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.service.MetadataService;
import com.arraywork.springforce.StaticResourceHandler;
import com.arraywork.springforce.error.HttpException;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    private MetadataService metadataService;

    @Value("${puffin.cover.base-dir}")
    private String coverBaseDir;

    // 封面资源
    @GetMapping("/cover/{id}.jpg")
    public void cover(HttpServletRequest request, HttpServletResponse response,
        @PathVariable String id) {
        try {
            request.setAttribute(StaticResourceHandler.ATTR_FILE, Path.of(coverBaseDir, id + ".jpg"));
            resourceHandler.handleRequest(request, response);
        } catch (ServletException | IOException e) {
            throw new HttpException(HttpStatus.NOT_FOUND);
        }
    }

    // 视频资源
    @GetMapping("/video/{code}")
    public void video(HttpServletRequest request, HttpServletResponse response,
        @PathVariable String code) {
        Metadata metadata = metadataService.getByCode(code);
        Path path = Path.of(metadata.getFilePath());
        try {
            request.setAttribute(StaticResourceHandler.ATTR_FILE, path);
            resourceHandler.handleRequest(request, response);
        } catch (ServletException | IOException e) {
            throw new HttpException(HttpStatus.NOT_FOUND);
        }
    }

}