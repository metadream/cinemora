package com.arraywork.cinemora;

import java.util.List;
import java.util.Map;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.metafield.Metafield;
import com.arraywork.cinemora.metafield.MetafieldManager;
import com.arraywork.cinemora.service.SettingService;

/**
 * 全局模板属性
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@ControllerAdvice
@ConfigurationProperties(prefix = "app")
public class ModelAttributes {

    private Map<String, String> app;

    @Resource
    private SettingService settingService;

    @Value("${app.name}")
    private String appName;

    @Value("${app.fullname}")
    private String appFullname;

    @Value("${app.description}")
    private String appDescription;

    // 应用属性  // TODO test
    @ModelAttribute("app")
    public Map<String, String> app() {
        return app;
    }

    // 应用名
    @ModelAttribute("appName")
    public String appName() {
        return appName;
    }

    // 应用名
    @ModelAttribute("appFullname")
    public String appFullname() {
        return appFullname;
    }

    // 应用名
    @ModelAttribute("appDescription")
    public String appDescription() {
        return appDescription;
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

    // 系统设置
    @ModelAttribute("settings")
    public Settings settings() {
        return settingService.getSettings();
    }

}