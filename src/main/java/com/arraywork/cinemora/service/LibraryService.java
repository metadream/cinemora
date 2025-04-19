package com.arraywork.cinemora.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arraywork.autumn.helper.DirectoryMonitor;
import com.arraywork.cinemora.entity.Settings;

import lombok.extern.slf4j.Slf4j;

/**
 * 媒体库服务
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
@Slf4j
public class LibraryService {

    private DirectoryMonitor monitor;
    @Resource
    private SettingService settingService;
    @Resource
    private MetadataService metadataService;

    @Autowired
    public LibraryService(LibraryListener listener) {
        monitor = new DirectoryMonitor(5000, listener);
    }

    // 随应用启动目录监听
    @PostConstruct
    public void scan() throws Exception {
        Settings settings = settingService.getSettings();
        if (settings != null) {
            String library = settings.getLibrary();
            scan(library, false);
        }
    }

    // 异步启动目录监听
    public void scan(String library, boolean emitOnStart) throws Exception {
        log.info("启动媒体库监听: {}", library);
        monitor.start(library);
    }

    // 重新扫描媒体库
    public void rescan() throws Exception {
        String library = settingService.getSettings().getLibrary();
        metadataService.purge(library);
        scan(library, true);
    }

    // 应用销毁时停止监听进程
    @PreDestroy
    public void onDestroyed() throws Exception {
        monitor.stop();
    }

}