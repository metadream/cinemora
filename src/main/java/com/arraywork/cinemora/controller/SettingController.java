package com.arraywork.cinemora.controller;

import jakarta.annotation.Resource;
import jakarta.validation.groups.Default;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arraywork.autumn.security.Permission;
import com.arraywork.autumn.security.SecurityController;
import com.arraywork.autumn.util.Validator;
import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.service.SettingService;

/**
 * 偏好设置控制器
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Controller
@RequestMapping("/~")
public class SettingController extends SecurityController {

    @Resource
    private SettingService settingService;

    // 初始化页面
    @GetMapping("/init")
    public String init() {
        Settings settings = settingService.getSettings();
        return settings != null ? "redirect:/" : "init";
    }

    // 设置页面
    @GetMapping("/settings")
    @Permission
    public String settings() {
        return "settings";
    }

    // 初始化偏好
    @PostMapping("/settings")
    @ResponseBody
    public Settings init(
        @Validated({ Default.class, Validator.Insert.class }) @RequestBody Settings settings) throws Exception {
        return settingService.init(settings);
    }

    // 保存偏好
    @PutMapping("/settings")
    @Permission
    @ResponseBody
    public Settings save(@Validated @RequestBody Settings settings) throws Exception {
        return settingService.save(settings);
    }

}