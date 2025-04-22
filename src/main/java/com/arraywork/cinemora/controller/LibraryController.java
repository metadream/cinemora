package com.arraywork.cinemora.controller;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arraywork.autumn.security.Permission;
import com.arraywork.cinemora.entity.ScanningOptions;
import com.arraywork.cinemora.service.LibraryService;

/**
 * 媒体库控制器
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/30
 */
@Controller
@RequestMapping("/~")
public class LibraryController {

    @Resource
    private LibraryService libraryService;

    /** 媒体库页面 */
    @GetMapping("/library")
    @Permission
    public String library() {
        return "library";
    }

    /** 扫描媒体库 */
    @PostMapping("/library")
    @Permission
    @ResponseBody
    public void scan(@RequestBody ScanningOptions options) throws Exception {
        libraryService.scan(options);
    }

    /** 取消扫描 */ // TODO test
    @PatchMapping("/library")
    @Permission
    @ResponseBody
    public void abort() {
        libraryService.setLockState(false);
    }

}