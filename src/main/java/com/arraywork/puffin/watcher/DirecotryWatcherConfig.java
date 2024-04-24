package com.arraywork.puffin.watcher;

import java.io.File;
import java.time.Duration;

import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PreDestroy;

/**
 * 目录监听配置
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/24
 */
@Configuration
public class DirecotryWatcherConfig {

    private static final String SOURCE_DIR = "/home/xehu/Documents/test";

    @Bean
    FileSystemWatcher fileSystemWatcher() {
        var fileSystemWatcher = new FileSystemWatcher(true, Duration.ofSeconds(3), Duration.ofSeconds(1));
        fileSystemWatcher.addSourceDirectory(new File(SOURCE_DIR));
        fileSystemWatcher.addListener(new FilesChangeListener());
        // fileSystemWatcher.setTriggerFilter(f -> f.toPath().endsWith(".csv"));
        fileSystemWatcher.start();
        return fileSystemWatcher;
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        fileSystemWatcher().stop();
    }

}
