package com.arraywork.puffin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.springforce.filewatch.DirectoryWatcher;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;

/**
 * 媒体库服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class LibraryService {

    private DirectoryWatcher watcher;
    @Resource
    private PreferenceService prefsService;
    @Resource
    private MetadataService metadataService;

    @Autowired
    public LibraryService(LibraryListener listener) {
        watcher = new DirectoryWatcher(2, 1, listener);
    }

    // 随应用启动目录监视器
    @PostConstruct
    public void scan() {
        Preference prefs = prefsService.getPreference();
        if (prefs != null) {
            String library = prefs.getLibrary();
            metadataService.purge(library);
            scan(library);
        }
    }

    // 异步启动目录监视器
    @Async
    public void scan(String library) {
        watcher.start(library);
    }

    // 应用销毁时停止监听进程
    @PreDestroy
    public void onDestroyed() {
        watcher.stop();
    }

}