package com.arraywork.puffin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.metafield.Metafield;
import com.arraywork.puffin.metafield.MetafieldManager;
import com.arraywork.puffin.service.PreferenceService;

import jakarta.annotation.Resource;

/**
 * 模板全局属性
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@ControllerAdvice
public class ModelAttributes {

    @Resource
    private MetafieldManager metafieldManager;
    @Resource
    private PreferenceService preferenceService;

    @Value("${puffin.app.name}")
    private String appName;

    // 应用名
    @ModelAttribute("appName")
    public String appName() {
        return appName;
    }

    // 所有元字段（首字母大写）
    @ModelAttribute("Metafields")
    public List<Metafield> Metafields() {
        return metafieldManager.getMetafields();
    }

    // 已选元字段（首字母小写）
    @ModelAttribute("metafields")
    public List<Metafield> metafields() {
        Preference preference = preferenceService.getPreference();
        return preference != null ? preference.getMetafields() : new ArrayList<>();
    }

}