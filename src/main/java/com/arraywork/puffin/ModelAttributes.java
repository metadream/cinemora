package com.arraywork.puffin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.arraywork.puffin.basedata.Censorship;
import com.arraywork.puffin.basedata.Quality;
import com.arraywork.puffin.basedata.Rating;
import com.arraywork.puffin.basedata.Region;

/**
 * Global Model Attributes
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@ControllerAdvice
public class ModelAttributes {

    @Value("${puffin.app.name}")
    private String appName;

    @ModelAttribute("appName")
    public String appName() {
        return appName;
    }

    @ModelAttribute("regions")
    public Region[] regions() {
        return Region.values();
    }

    @ModelAttribute("qualities")
    public Quality[] qualities() {
        return Quality.values();
    }

    @ModelAttribute("censorships")
    public Censorship[] censorships() {
        return Censorship.values();
    }

    @ModelAttribute("ratings")
    public Rating[] ratings() {
        return Rating.values();
    }

}