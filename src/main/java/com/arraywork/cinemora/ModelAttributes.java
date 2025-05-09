package com.arraywork.cinemora;

import java.util.List;
import java.util.Map;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
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
public class ModelAttributes {

    @Resource
    private SettingService settingService;

    @Value("${app.version}")
    private String version;

    @Value("${app.name}")
    private String appName;

    @Value("${app.fullname}")
    private String fullname;

    @Value("${app.logo}")
    private String logo;

    @Value("${app.vast:}")
    private String vast;

    @Value("${app.description}")
    private String description;

    @Value("${app.copyright}")
    private String copyright;

    @Value("${app.hide-login}")
    private boolean hideLogin;

    // 应用属性
    @ModelAttribute("app")
    public Map<String, Object> app() {
        return Map.of(
            "name", appName,
            "fullname", fullname,
            "version", version,
            "logo", logo,
            "vast", vast,
            "description", description,
            "copyright", copyright,
            "hideLogin", hideLogin
        );
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