package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arraywork.puffin.entity.Library;
import com.arraywork.puffin.service.LibraryService;

import jakarta.annotation.Resource;

/**
 * 媒体库控制器
 * @author AiChen
 * @created 2024/04/22
 */
@Controller
public class LibraryController {

    @Resource
    private LibraryService libraryService;

    @GetMapping("/libraries")
    public String libraries(Model model) {
        model.addAttribute("libraries", libraryService.getLibraries());
        return "libraries";
    }

    @PutMapping("/library")
    @ResponseBody
    public Library save(@Validated @RequestBody Library library) {
        return libraryService.save(library);
    }

}