package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

    // 首页
    @GetMapping("/")
    public String index() {
        Preference preference = preferenceService.getPreference();
        return preference != null ? "index" : "redirect:init";
    }

    // 初始化页面
    @GetMapping("/init")
    public String init() {
        return "init";
    }

    // 初始化接口
    @PostMapping("/init")
    @ResponseBody
    public Preference init(@Validated @RequestBody Preference preference) {
        return preferenceService.init(preference);
    }

}