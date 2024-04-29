package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    // 详情页
    @GetMapping("/{code}")
    public String thread(Model model, @PathVariable String code) {
        model.addAttribute("metadata", metadataService.getByCode(code));
        return "thread";
    }

}