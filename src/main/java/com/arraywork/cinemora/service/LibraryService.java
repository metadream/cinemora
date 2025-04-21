package com.arraywork.cinemora.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
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

    private static final List<ScanningLog> scanningLogs = new CopyOnWriteArrayList<>();

    private static final String CHANNEL_NAME = "library";
    private static final AtomicBoolean isAborted = new AtomicBoolean(false);
    private static final AtomicBoolean isScanning = new AtomicBoolean(false);
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

    // 统计所有文件总数（1000个文件以上建议使用并行流）TODO 不加并行流速度测试
    private long countRegularFiles(Path path) {
        AtomicLong count = new AtomicLong();
        try (Stream<Path> paths = Files.walk(path).parallel()) {
            count.set(paths.filter(Files::isRegularFile).count());
        } catch (IOException e) {
            log.error("Error counting files in directory: {}", path, e);
            throw new RuntimeException("Error counting files in directory: " + path, e);
        }
        return count.get();
    }

    /** 异步扫描媒体库 */
    @Async
    public void scan(ScanningOptions options) throws IOException {
        //        public void criticalMethod() {
        //            if (!isScanning.compareAndSet(false, true)) {
        //                return; // 已经有线程在执行
        //            }
        //
        //            try {
        //                // 临界区代码
        //            } finally {
        //                isScanning.set(false);
        //            }
        //        }

        Path library = settingService.getLibrary();
        long count = countRegularFiles(library);
        if (count == 0) {
            ScanningLog log = new ScanningLog();
            log.setAction(ScanningAction.SCAN);
            log.setResult(ScanningResult.FINISHED);
            log.setMessage("No files found.");
            channelService.broadcast(CHANNEL_NAME, log);
            return;
        }

        AtomicLong ordinal = new AtomicLong();
        AtomicLong indexed = new AtomicLong();
        AtomicLong reindexed = new AtomicLong();
        AtomicLong skipped = new AtomicLong();
        AtomicLong failed = new AtomicLong();
        long st = System.currentTimeMillis();

        // 遍历文件
        Files.walkFileTree(library, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                if (isAborted.get()) {
                    return FileVisitResult.TERMINATE;
                }
                if (attrs.isRegularFile()) {
                    ordinal.incrementAndGet();
                    ScanningResult result = process(ScanningAction.SCAN, path.toFile(), options.isForceReindexing());
                    switch (result) {
                        case INDEXED -> indexed.incrementAndGet();
                        case REINDEXED -> reindexed.incrementAndGet();
                        case SKIPPED -> skipped.incrementAndGet();
                        case FAILED -> failed.incrementAndGet();
                    }
                    //                    channelService.broadcast(CHANNEL_NAME, "realtime", log);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        System.out.println("ffmpeg============" + (System.currentTimeMillis() - st));
        ScanningLog log = new ScanningLog();
        log.setAction(ScanningAction.SCAN);
        log.setResult(ScanningResult.FINISHED);
        log.setMessage(count + " files found: " + indexed + " indexed, " + skipped + " skipped, and " + failed + " failed.");
        channelService.broadcast(CHANNEL_NAME, log);
    }

    // TODO 涵盖所有ScanningResult，例如reindexed
    public synchronized ScanningResult process(ScanningAction action, File file, boolean isForceReIndexing) {
        Path library = settingService.getLibrary();
        String relativePath = library.relativize(file.toPath()).toString();
        ScanningResult result;

        ScanningLog log = new ScanningLog();
        log.setAction(action);
        log.setMessage(relativePath);

        try {
            Metadata metadata = metadataService.build(file, isForceReIndexing);
            if (metadata != null) {
                log.setResult(ScanningResult.INDEXED);
                result = ScanningResult.INDEXED;
            } else {
                log.setResult(ScanningResult.SKIPPED);
                result = ScanningResult.SKIPPED;
            }
        } catch (Exception e) {
            log.setResult(ScanningResult.FAILED);
            result = ScanningResult.FAILED;
        }

        channelService.broadcast(CHANNEL_NAME, log);
        return result;
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