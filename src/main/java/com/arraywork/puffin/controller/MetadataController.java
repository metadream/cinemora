package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.arraywork.puffin.service.PreferenceService;

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
    private PreferenceService preferenceService;

    // 元数据页面
    @GetMapping("/metadata")
    public String metadata() {
        return "metadata";
    }

}