package com.arraywork.puffin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.arraywork.puffin.basedata.Censorship;
import com.arraywork.puffin.basedata.Quality;
import com.arraywork.puffin.basedata.Rating;
import com.arraywork.puffin.basedata.Region;

/**
 * 模板全局属性
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@ControllerAdvice
public class ModelAttributes {

    @Value("${puffin.app.name}")
    private String appName;

    // 应用名
    @ModelAttribute("appName")
    public String appName() {
        return appName;
    }

    // 地区枚举
    @ModelAttribute("regions")
    public Region[] regions() {
        return Region.values();
    }

    // 画质枚举
    @ModelAttribute("qualities")
    public Quality[] qualities() {
        return Quality.values();
    }

    // 审查枚举
    @ModelAttribute("censorships")
    public Censorship[] censorships() {
        return Censorship.values();
    }

    // 分级枚举
    @ModelAttribute("ratings")
    public Rating[] ratings() {
        return Rating.values();
    }

}