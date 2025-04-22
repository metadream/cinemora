package com.arraywork.cinemora.service;

import java.io.File;
import jakarta.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.arraywork.autumn.helper.DirectoryMonitor;

import lombok.extern.slf4j.Slf4j;

/**
 * 媒体库监听器
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/27
 */
@Component
@Slf4j
public class LibraryListener extends DirectoryMonitor.DirectoryListener {

    @Resource
    @Lazy
    private LibraryService libraryService;

    @Override
    public void onFileCreate(File file) {
        libraryService.processFile(file);
    }

    @Override
    public void onFileChange(File file) {
        libraryService.processFile(file);
    }

    @Override
    public void onFileDelete(File file) {
        libraryService.deleteFile(file);
    }

}