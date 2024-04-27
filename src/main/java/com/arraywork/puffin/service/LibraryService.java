package com.arraywork.puffin.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.arraywork.springforce.filewatch.DirectoryWatcher;
import com.arraywork.springforce.filewatch.FileSystemListener;
import com.arraywork.springforce.util.CommonUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;

/**
 * 媒体库服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class LibraryService implements FileSystemListener {

    // 创建目录监视器
    private DirectoryWatcher watcher = new DirectoryWatcher(3, 1, this);

    @Resource
    private MetadataService metadataService;

    @Value("${puffin.dir.cover}")
    private String coverDir;

    // 异步启动目录监视器
    @Async
    public void scan(String rootDir) {
        watcher.start(rootDir);
    }

    @Override
    public void onStarted(File file, int count, int total) {
        System.out.print("Started[" + count + "/" + total + "]: ");
        System.out.println(file);

        CommonUtils.delay(500);
        metadataService.create(file);
    }

    @Override
    public void onAdded(File file) {
        System.out.print("Added: ");
        System.out.println(file);

        CommonUtils.delay(500);
        metadataService.create(file);
    }

    @Override
    public void onModified(File file) {
        System.out.print("Modified: ");
        System.out.println(file);
    }

    @Override
    public void onDeleted(File file) {
        System.out.print("Deleted: ");
        System.out.println(file);
    }

    // 应用销毁时停止监听进程
    @PreDestroy
    public void onDestroyed() {
        watcher.stop();
    }

}