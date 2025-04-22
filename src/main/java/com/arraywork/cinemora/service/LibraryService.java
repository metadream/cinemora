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
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.arraywork.autumn.channel.ChannelService;
import com.arraywork.autumn.helper.DirectoryMonitor;
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

    private static final List<EventLog> EVENT_LOGS = new CopyOnWriteArrayList<>();  // TODO
    private static final String CHANNEL_NAME = "library";
    private static final AtomicBoolean isThreadLocked = new AtomicBoolean(false);
    private DirectoryMonitor monitor;

    @Resource
    private ChannelService channelService;
    @Resource
    private SettingService settingService;
    @Resource
    private MetadataService metadataService;

    @Autowired
    public LibraryService(LibraryListener listener) {
        monitor = new DirectoryMonitor(5000, listener);
    }

    /** 随应用启动媒体库监听 */
    @PostConstruct
    public void scan() throws Exception {
        Settings settings = settingService.getSettings();
        if (settings != null) {
            String library = settings.getLibrary();
            monitor.start(library);
            log.info("Library monitor started, watching {}", library);
        }
    }

    /** 异步扫描媒体库 */
    @Async
    public void scan(ScanningOptions options) throws IOException {
        // 锁定状态 TODO test
        lockThreadState(true);

        // 统计文件总数
        Path library = settingService.getLibrary();
        long total = 0;//FileUtils.countRegularFiles(library);
        if (total == 0) {  // TODO test
            EventLog eventLog = new EventLog();
            eventLog.setSource(EventSource.SCANNING);
            eventLog.setState(EventState.FINISHED);
            eventLog.setHint("No files found.");
            emitLog(eventLog);
            lockThreadState(false);
            return;
        }

        // 清理无效索引
        if (options.isCleanIndexes()) {
            cleanIndexes();
        }

        // 统计扫描结果
        AtomicLong count = new AtomicLong();
        AtomicLong indexed = new AtomicLong();
        AtomicLong reindexed = new AtomicLong();
        AtomicLong skipped = new AtomicLong();
        AtomicLong failed = new AtomicLong();
        long st = System.currentTimeMillis();

        // 遍历文件
        Files.walkFileTree(library, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                if (!isThreadLocked.get()) {  // TODO 测试取消扫描
                    return FileVisitResult.TERMINATE;
                }
                if (attrs.isRegularFile()) {
                    count.incrementAndGet();
                    EventState state = process(EventSource.SCANNING,
                        path.toFile(), count.get(), total,
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

        System.out.println("ffmpeg============" + (System.currentTimeMillis() - st));
        EventLog eventLog = new EventLog();
        eventLog.setTotal(total);
        eventLog.setIndexed(indexed.get());
        eventLog.setReindexed(reindexed.get());
        eventLog.setSkipped(skipped.get());
        eventLog.setFailed(failed.get());
        eventLog.setSource(EventSource.SCANNING);
        eventLog.setState(EventState.FINISHED);
        emitLog(eventLog);

        lockThreadState(false);
    }

    /** 处理文件（监听接口使用） */
    public EventState process(File file) {
        return process(EventSource.LISTENING, file, 0, 0, true);
    }

    /** 处理文件 */
    public synchronized EventState process(EventSource source, File file, long count, long total, boolean isForceReIndexing) {
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

    /** 清理元数据 */  // TODO test
    private void cleanIndexes() {
        List<Metadata> orphanedList = metadataService.getOrphanedMetadata();
        int count = 0, deleted = 0, failed = 0, total = orphanedList.size();

        // 清理过程及日志
        for (Metadata metadata : orphanedList) {
            EventLog eventLog = new EventLog();
            eventLog.setSource(EventSource.CLEANING);
            eventLog.setPath(metadata.getFilePath());
            eventLog.setCount(++count);
            eventLog.setTotal(total);

            try {
                metadataService.delete(metadata);
                eventLog.setDeleted(++deleted);
                eventLog.setState(EventState.DELETED);
            } catch (Exception e) {
                eventLog.setFailed(++failed);
                eventLog.setState(EventState.FAILED);
                eventLog.setHint(e.getMessage());
            }
            emitLog(eventLog);
        }

        // 清理完成的日志
        EventLog eventLog = new EventLog();
        eventLog.setTotal(total);
        eventLog.setDeleted(deleted);
        eventLog.setFailed(failed);
        eventLog.setSource(EventSource.CLEANING);
        eventLog.setState(EventState.FINISHED);
        emitLog(eventLog);
    }

    /** 设置异步线程锁定状态 */
    public void lockThreadState(boolean newValue) {
        boolean oldValue = isThreadLocked.get();
        if (isThreadLocked.compareAndSet(oldValue, newValue)) {
            channelService.broadcast(CHANNEL_NAME, "state", newValue);
        }
    }

    /** 应用销毁时停止监听进程 */
    @PreDestroy
    public void onDestroyed() throws Exception {
        monitor.stop();
    }

    /** 发送日志 */
    private void emitLog(EventLog eventLog) {
        channelService.broadcast(CHANNEL_NAME, eventLog);
    }

}