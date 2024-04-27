package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.service.PreferenceService;

import jakarta.annotation.Resource;

/**
 * 偏好设置控制器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Controller
public class PreferenceController {

    @Resource
    private PreferenceService prefService;

    // 初始化偏好
    @PostMapping("/preference")
    @ResponseBody
    public Preference init(@Validated @RequestBody Preference pref) {
        return prefService.init(pref);
    }

    // 保存偏好
    @PutMapping("/preference")
    @ResponseBody
    public Preference save(@Validated @RequestBody Preference pref) {
        return prefService.save(pref);
    }

}