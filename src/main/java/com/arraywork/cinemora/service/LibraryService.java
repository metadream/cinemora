package com.arraywork.cinemora.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.arraywork.autumn.channel.ChannelService;
import com.arraywork.autumn.helper.DirectoryMonitor;
import com.arraywork.cinemora.entity.Metadata;
import com.arraywork.cinemora.entity.ScanningLog;
import com.arraywork.cinemora.entity.ScanningOptions;
import com.arraywork.cinemora.enums.ScanningAction;
import com.arraywork.cinemora.enums.ScanningResult;

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

    private static final String CHANNEL_NAME = "library";
    private DirectoryMonitor monitor;

    @Resource
    private ChannelService channelService;
    @Resource
    private FfmpegService ffmpegService;
    @Resource
    private SettingService settingService;
    @Resource
    private MetadataService metadataService;

    @Autowired
    public LibraryService(LibraryListener listener) {
        monitor = new DirectoryMonitor(5000, listener);
    }

    // 随应用启动目录监听
    //    @PostConstruct
    //    public void scan() throws Exception {
    //        Settings settings = settingService.getSettings();
    //        if (settings != null) {
    //            String library = settings.getLibrary();
    //            scan(library, false);
    //        }
    //    }

    // 异步启动目录监听
    @Async
    public void scan(ScanningOptions options) throws IOException {
        Path library = Path.of(settingService.getSettings().getLibrary());
        AtomicLong count = new AtomicLong();
        AtomicLong total = new AtomicLong();
        AtomicLong indexed = new AtomicLong();
        AtomicLong skipped = new AtomicLong();
        AtomicLong failed = new AtomicLong();

        // 统计所有文件总数
        try (Stream<Path> paths = Files.walk(library)) {
            total.set(paths.filter(Files::isRegularFile).count());
        }

        System.out.println(options);
        long st = System.currentTimeMillis();
        // 遍历文件
        Files.walkFileTree(library, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                ScanningLog log = new ScanningLog(ScanningAction.SCAN);
                log.setCount(count.incrementAndGet());
                log.setTotal(total.get());
                log.setMessage(library.relativize(path).toString());

                try {
                    Metadata metadata = metadataService.build(path.toFile(), options.isForceRebuild());
                    if (metadata != null) {
                        log.setResult(ScanningResult.SUCCEEDED);
                        indexed.incrementAndGet();
                    } else {
                        log.setResult(ScanningResult.SKIPPED);
                        skipped.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.setResult(ScanningResult.FAILED);
                    failed.incrementAndGet();
                }

                channelService.broadcast(CHANNEL_NAME, log);
                return FileVisitResult.CONTINUE;
            }
        });

        System.out.println("ffmpeg============" + (System.currentTimeMillis() - st));

        ScanningLog log = new ScanningLog(ScanningAction.SCAN);
        log.setResult(ScanningResult.FINISHED);
        log.setMessage(total + " files found: " + indexed + " indexed, " + skipped + " skipped, and " + failed + " failed.");
        channelService.broadcast(CHANNEL_NAME, log);
    }

    // 重新扫描媒体库
    //    public void rescan() throws Exception {
    //        String library = settingService.getSettings().getLibrary();
    //        metadataService.purge(library);
    //        scan(library, true);
    //    }

    // 应用销毁时停止监听进程
    @PreDestroy
    public void onDestroyed() throws Exception {
        monitor.stop();
    }

}