package com.arraywork.cinemora;

import java.util.List;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.metafield.Metafield;
import com.arraywork.cinemora.metafield.MetafieldManager;
import com.arraywork.cinemora.service.SettingService;

/**
 * 模板全局属性
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@ControllerAdvice
public class ModelAttributes {

    @Resource
    private SettingService settingService;

    @Value("${app.name}")
    private String appName;

    // 应用名
    @ModelAttribute("appName")
    public String appName() {
        return appName;
    }

    // 所有元字段实体
    @ModelAttribute("Metafields")
    public List<Metafield> Metafields() {
        return MetafieldManager.getMetafields();
    }

    // 已选元字段名称
    @ModelAttribute("metafields")
    public List<String> metafields() {
        Settings settings = settings();
        return settings != null ? settings.getMetafields().stream().map(Metafield::getName).toList() : null;
    }

    // 偏好设置
    @ModelAttribute("settings")
    public Settings settings() {
        return settingService.getSettings();
    }

}