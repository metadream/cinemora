package com.arraywork.puffin;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.metafield.Metafield;
import com.arraywork.puffin.metafield.MetafieldManager;
import com.arraywork.puffin.service.PreferenceService;
import com.arraywork.springforce.util.Arrays;

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
    private PreferenceService prefsService;

    @Value("${puffin.app.name}")
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
        Preference prfc = preference();
        return prfc != null ? Arrays.map(prfc.getMetafields(), Metafield::getName) : null;
    }

    // 偏好设置
    @ModelAttribute("preference")
    public Preference preference() {
        return prefsService.getPreference();
    }

}