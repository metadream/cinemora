package com.arraywork.puffin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.arraywork.springforce.filewatch.DirectoryWatcher;

import jakarta.annotation.PreDestroy;

/**
 * 媒体库服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class LibraryService {

    // 创建目录监视器
    private DirectoryWatcher watcher;

    @Autowired
    public LibraryService(LibraryListener listener) {
        watcher = new DirectoryWatcher(3, 1, listener);
    }

    // 异步启动目录监视器
    @Async
    public void scan(String rootDir) {
        watcher.start(rootDir);
    }

    // 应用销毁时停止监听进程
    @PreDestroy
    public void onDestroyed() {
        watcher.stop();
    }

}