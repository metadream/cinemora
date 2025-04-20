package com.arraywork.cinemora.controller;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arraywork.autumn.channel.SseChannel;
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
    private SseChannel channel;
    @Resource
    private LibraryService libraryService;

    // 页面
    @GetMapping("/library")
    @Permission
    public String library() {
        return "library";
    }

    // 扫描媒体库
    @PostMapping("/library")
    @Permission
    @ResponseBody
    public void scan(@RequestBody ScanningOptions options) throws Exception {
        libraryService.scan(options);
    }

    // SSE获取扫描状态
    //    @GetMapping("/status")
    //    @Permission
    //    @ResponseBody
    //    public SseEmitter status() {
    //        return channel.subscribe();
    //    }

    // 重新扫描媒体库
    //    @PostMapping("/rescan")
    //    @Permission
    //    @ResponseBody
    //    public void rescan() throws Exception {
    //        libraryService.rescan();
    //    }

}