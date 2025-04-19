package com.arraywork.cinemora.controller;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.arraywork.autumn.id.KeyGenerator;
import com.arraywork.cinemora.entity.Metadata;
import com.arraywork.cinemora.service.MetadataService;
import com.arraywork.cinemora.service.TagCloudService;

/**
 * 门户控制器
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Controller
public class PortalController {

    @Resource
    private MetadataService metadataService;
    @Resource
    private TagCloudService tagCloudService;

    // 首页
    @GetMapping("/")
    public String index(Model model, String page, Metadata condition) {
        model.addAttribute("condition", condition);
        model.addAttribute("pagination", metadataService.getMetadatas(page, condition));
        return "index";
    }

    // 精选页
    @GetMapping("/starred")
    public String starred(Model model, String page) {
        Metadata condition = new Metadata();
        condition.setStarred(true);
        model.addAttribute("pagination", metadataService.getMetadatas(page, condition));
        return "index";
    }

    // 发现页
    @GetMapping("/explore")
    public String explore(Model model) {
        model.addAttribute("tagCloud", tagCloudService.getTagCloud());
        return "explore";
    }

    // 详情页
    @GetMapping("/{code}")
    public String thread(Model model, @PathVariable String code) {
        model.addAttribute("metadata", metadataService.getByCode(code));
        model.addAttribute("transId", KeyGenerator.nanoId());
        return "thread";
    }

}