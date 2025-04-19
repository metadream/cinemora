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
import com.arraywork.autumn.util.Validator;
import com.arraywork.cinemora.entity.Preference;
import com.arraywork.cinemora.service.PreferenceService;

/**
 * 偏好设置控制器
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Controller
@RequestMapping("/~")
public class PreferenceController {

    @Resource
    private PreferenceService prefsService;

    // 初始化页面
    @GetMapping("/init")
    public String init() {
        Preference prefs = prefsService.getPreference();
        return prefs != null ? "redirect:/" : "init";
    }

    // 初始化偏好
    @PostMapping("/preference")
    @ResponseBody
    public Preference init(
        @Validated({ Default.class, Validator.Insert.class }) @RequestBody Preference prefs) throws Exception {
        return prefsService.init(prefs);
    }

    // 保存偏好
    @PutMapping("/preference")
    @Permission
    @ResponseBody
    public Preference save(@Validated @RequestBody Preference prefs) throws Exception {
        return prefsService.save(prefs);
    }

}