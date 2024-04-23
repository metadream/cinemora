package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.service.PreferenceService;

import jakarta.annotation.Resource;

/**
 * 偏好控制器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Controller
public class PreferenceController {

    @Resource
    private PreferenceService preferenceService;

    // 保存偏好
    @PutMapping("/preference")
    @ResponseBody
    public Preference preference(@Validated @RequestBody Preference preference) {
        return preferenceService.save(preference);
    }

}