package com.arraywork.puffin.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.service.MetadataService;

import jakarta.annotation.Resource;

/**
 * 元数据控制器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
@Controller
@RequestMapping("/~")
public class MetadataController {

    @Resource
    private MetadataService metadataService;

    // 元数据页面
    @GetMapping("/metadata")
    public String metadata(Model model, String page, Metadata condition) {
        model.addAttribute("condition", condition);
        model.addAttribute("pagination", metadataService.getMetadatas(page, condition));
        return "metadata";
    }

    // 元数据接口
    @PutMapping("/metadata")
    @ResponseBody
    public Metadata metadata(@Validated @RequestBody Metadata metadata) {
        return metadataService.save(metadata);
    }

    // 上传封面图片
    @PutMapping("/metadata/{id}")
    @ResponseBody
    public String upload(@PathVariable String id, MultipartFile multipartFile) throws IOException {
        return metadataService.upload(id, multipartFile);
    }

}