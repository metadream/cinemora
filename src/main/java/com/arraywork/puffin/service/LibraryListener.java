package com.arraywork.puffin.service;

import java.io.File;

import org.springframework.stereotype.Component;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.entity.ScanningInfo;
import com.arraywork.puffin.enums.ScanEvent;
import com.arraywork.puffin.enums.ScanState;
import com.arraywork.springforce.SseChannel;
import com.arraywork.springforce.filewatch.FileSystemListener;
import com.arraywork.springforce.util.Times;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 媒体库监听器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/27
 */
@Component
@Slf4j
public class LibraryListener implements FileSystemListener {

    private int success;
    private int skipped;
    private int failed;

    @Resource
    private MetadataService metadataService;
    @Resource
    private SseChannel channel;

    // 监听启动回调方法
    @Override
    public void onStarted(File file, int count, int total) {
        onChanged(file, count, total, ScanEvent.SCAN, () -> metadataService.build(file, false));
    }

    // 新增文件回调方法
    @Override
    public void onAdded(File file, int count, int total) {
        onChanged(file, count, total, ScanEvent.ADD, () -> metadataService.build(file, false));
    }

    // 修改文件回调方法
    @Override
    public void onModified(File file, int count, int total) {
        onChanged(file, count, total, ScanEvent.MODIFY, () -> metadataService.build(file, true));
    }

    // 删除文件回调方法
    @Override
    public void onDeleted(File file, int count, int total) {
        onChanged(file, count, total, ScanEvent.DELETE, () -> metadataService.delete(file));
    }

    // 监听回调方法
    private void onChanged(File file, int count, int total, ScanEvent event, Consumer consumer) {
        Times.delay(200);
        ScanningInfo info = new ScanningInfo(event);
        info.count = count;
        info.total = total;
        info.path = file.getPath();

        try {
            Metadata metadata = consumer.execute();
            if (metadata != null) {
                info.state = ScanState.SUCCESS;
                success++;
            } else {
                info.state = ScanState.SKIPPED;
                skipped++;
            }
        } catch (Exception e) {
            info.state = ScanState.FAILED;
            info.message = e.getMessage();
            failed++;
            log.error("Build '{}' error: ", file, e);
        } finally {
            channel.broadcast(info);

            if (count == total) {
                info = new ScanningInfo(event);
                info.state = ScanState.FINISHED;
                info.message = "本次扫描共发现" + total + "个文件。"
                    + "成功" + success + "个，跳过" + skipped + "个，失败" + failed + "个。";
                channel.broadcast(info);
                success = 0;
                skipped = 0;
                failed = 0;
            }
        }
    }

    // 方法接口：监听回调需执行的逻辑
    interface Consumer {

        Metadata execute();

    }

}