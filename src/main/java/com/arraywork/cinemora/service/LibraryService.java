package com.arraywork.cinemora.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.websocket.Session;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.arraywork.autumn.channel.ChannelService;
import com.arraywork.autumn.helper.DirectoryMonitor;
import com.arraywork.autumn.util.FileUtils;
import com.arraywork.cinemora.entity.EventLog;
import com.arraywork.cinemora.entity.Metadata;
import com.arraywork.cinemora.entity.ScanningOptions;
import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.enums.EventSource;
import com.arraywork.cinemora.enums.EventState;

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
    private static final AtomicBoolean isThreadLocked = new AtomicBoolean(false);

    @Resource
    private DirectoryMonitor libraryMonitor;
    @Resource
    private ChannelService channelService;
    @Resource
    private SettingService settingService;
    @Resource
    private MetadataService metadataService;

    /** 应用启动后监听媒体库 */
    @EventListener(ApplicationReadyEvent.class)
    public void listen() throws Exception {
        Settings settings = settingService.getSettings();
        if (settings != null) {
            String library = settings.getLibrary();
            libraryMonitor.start(library);
            log.info("Library monitor started, watching {}", library);
        }
    }

    /** Send thread state when channel is opened */
    @PostConstruct
    public void sendThreadState() {
        channelService.onOpen((String channel, Session session) -> {
            channelService.sendMessage(session, "state", isThreadLocked.get());
        });
    }

    /** 异步扫描媒体库 */
    @Async
    public void scan(ScanningOptions options) throws IOException {
        // 锁定线程状态防止并发
        if (!lockScanThread()) return;

        // 统计扫描结果
        AtomicLong count = new AtomicLong();
        AtomicLong total = new AtomicLong();
        AtomicLong indexed = new AtomicLong();
        AtomicLong reindexed = new AtomicLong();
        AtomicLong skipped = new AtomicLong();
        AtomicLong deleted = new AtomicLong();
        AtomicLong failed = new AtomicLong();

        // 清理无效索引
        if (options.isCleanIndexes()) {
            List<Metadata> orphanedList = metadataService.getOrphanedMetadata();
            total.set(orphanedList.size());

            for (Metadata metadata : orphanedList) {
                count.incrementAndGet();
                EventState state = cleanIndex(metadata, count.get(), total.get());
                switch (state) {
                    case DELETED -> deleted.incrementAndGet();
                    case FAILED -> failed.incrementAndGet();
                }
            }
        }

        // 统计媒体库文件总数
        Path library = settingService.getLibrary();
        long totalFiles = FileUtils.countRegularFiles(library);
        if (totalFiles == 0) {
            EventLog eventLog = new EventLog();
            eventLog.setSource(EventSource.SCANNING);
            eventLog.setState(EventState.FINISHED);
            eventLog.setHint("No files found.");
            emitLog(eventLog);
            unlockScanThread();
            return;
        }

        // 遍历文件
        count.set(0);
        total.set(totalFiles);
        Files.walkFileTree(library, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                if (!isThreadLocked.get()) {
                    return FileVisitResult.TERMINATE;
                }
                if (attrs.isRegularFile()) {
                    count.incrementAndGet();
                    EventState state = processFile(EventSource.SCANNING,
                        path.toFile(), count.get(), total.get(),
                        options.isForceReindexing());
                    switch (state) {
                        case INDEXED -> indexed.incrementAndGet();
                        case REINDEXED -> reindexed.incrementAndGet();
                        case SKIPPED -> skipped.incrementAndGet();
                        case FAILED -> failed.incrementAndGet();
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        EventLog eventLog = new EventLog();
        eventLog.setTotal(total.get());
        eventLog.setIndexed(indexed.get());
        eventLog.setReindexed(reindexed.get());
        eventLog.setFailed(deleted.get());
        eventLog.setSkipped(skipped.get());
        eventLog.setFailed(failed.get());
        eventLog.setSource(EventSource.SCANNING);
        eventLog.setState(EventState.FINISHED);
        emitLog(eventLog);
        unlockScanThread();
    }

    /** 处理文件（监听接口使用） */
    public EventState processFile(File file) {
        return processFile(EventSource.LISTENING, file, 0, 0, true);
    }

    /** 处理文件 */
    public synchronized EventState processFile(EventSource source, File file, long count, long total, boolean isForceReIndexing) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        Path library = settingService.getLibrary();
        String relativePath = library.relativize(file.toPath()).toString();

        EventState state;
        EventLog eventLog = new EventLog();
        eventLog.setSource(source);
        eventLog.setPath(relativePath);
        eventLog.setCount(count);
        eventLog.setTotal(total);

        try {
            state = metadataService.build(file, isForceReIndexing);
        } catch (Exception e) {
            state = EventState.FAILED;
            eventLog.setHint(e.getMessage());
        }

        eventLog.setState(state);
        emitLog(eventLog);
        return state;
    }

    /** 清理元数据 */
    private EventState cleanIndex(Metadata metadata, long count, long total) {
        EventState state;
        EventLog eventLog = new EventLog();
        eventLog.setSource(EventSource.CLEANING);
        eventLog.setPath(metadata.getFilePath());
        eventLog.setCount(count);
        eventLog.setTotal(total);

        try {
            metadataService.delete(metadata);
            state = EventState.DELETED;
        } catch (Exception e) {
            state = EventState.FAILED;
            eventLog.setHint(e.getMessage());
        }

        eventLog.setState(state);
        emitLog(eventLog);
        return state;
    }

    /** 删除媒体库文件 */
    public EventState deleteFile(File file) {
        Path library = settingService.getLibrary();
        EventLog eventLog = new EventLog();
        eventLog.setSource(EventSource.LISTENING);
        try {
            eventLog.setState(metadataService.delete(file));
            eventLog.setPath(library.relativize(file.toPath()).toString());
        } catch (Exception e) {
            eventLog.setHint(e.getMessage());
            eventLog.setState(EventState.FAILED);
        }
        emitLog(eventLog);
        return eventLog.getState();
    }

    /** 锁定扫描线程 */
    public boolean lockScanThread() {
        boolean locked = isThreadLocked.compareAndSet(false, true);
        channelService.broadcast(CHANNEL_NAME, "state", true);
        return locked;
    }

    /** 解锁扫描线程 */
    public void unlockScanThread() {
        isThreadLocked.set(false);
        channelService.broadcast(CHANNEL_NAME, "state", false);
    }

    /** 应用销毁时停止监听进程 */
    @PreDestroy
    public void onDestroyed() throws Exception {
        libraryMonitor.stop();
    }

    /** 发送日志 */
    private void emitLog(EventLog eventLog) {
        channelService.broadcast(CHANNEL_NAME, eventLog);
    }

}