package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.arraywork.springforce.SseChannel;
import com.arraywork.springforce.security.Authority;

import jakarta.annotation.Resource;

/**
 * 媒体库控制器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/30
 */
@Controller
@RequestMapping("/~")
public class LibraryController {

    @Resource
    private SseChannel channel;

    // SSE获取扫描状态
    @GetMapping("/status")
    @Authority
    @ResponseBody
    public SseEmitter status() {
        return channel.subscribe();
    }

}