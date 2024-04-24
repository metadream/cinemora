package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.service.PreferenceService;

import jakarta.annotation.Resource;

/**
 * 首页控制器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Controller
public class IndexController {

    @Resource
    private PreferenceService preferenceService;

    // 初始化页面
    @GetMapping("/init")
    public String init() {
        Preference preference = preferenceService.getPreference();
        return preference != null ? "redirect:/" : "init";
    }

}