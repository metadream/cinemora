package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.service.MetadataService;

import jakarta.annotation.Resource;

/**
 * 门户控制器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Controller
public class PortalController {

    @Resource
    private MetadataService metadataService;

    // 首页
    @GetMapping("/")
    public String index(Model model, String page, Metadata condition) {
        model.addAttribute("pagination", metadataService.getMetadatas(page, condition));
        return "index";
    }

}