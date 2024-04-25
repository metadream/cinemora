package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
public class MetadataController {

    @Resource
    private MetadataService metadataService;

    // 元数据页面
    @GetMapping("/metadata")
    public String metadata(Model model, String page, Metadata condition) {
        model.addAttribute("pagination", metadataService.getMetadatas(page, condition));
        return "metadata";
    }

}