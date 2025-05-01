package com.arraywork.cinemora.controller;

import java.io.IOException;
import jakarta.annotation.Resource;

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

import com.arraywork.autumn.security.Permission;
import com.arraywork.cinemora.entity.Metadata;
import com.arraywork.cinemora.service.MetadataService;

/**
 * 元数据控制器
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
@Controller
@RequestMapping("/~")
public class MetadataController {

    @Resource
    private MetadataService metadataService;

    /** 元数据页面 */
    @GetMapping("/metadata")
    @Permission
    public String metadata(Model model, String page, Metadata condition) {
        condition.setHidden(true);
        model.addAttribute("pagination", metadataService.getMetadata(page, condition));
        model.addAttribute("condition", condition);
        return "metadata";
    }

    /** 保存元数据 */
    @PutMapping("/metadata")
    @Permission
    @ResponseBody
    public Metadata metadata(@Validated @RequestBody Metadata metadata) {
        return metadataService.save(metadata);
    }

    /** 上传封面图片 */
    @PutMapping("/metadata/{id}")
    @Permission
    @ResponseBody
    public String upload(@PathVariable String id, MultipartFile multipartFile) throws IOException {
        return metadataService.upload(id, multipartFile);
    }

}