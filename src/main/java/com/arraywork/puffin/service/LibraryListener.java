package com.arraywork.puffin.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.entity.Scanning;
import com.arraywork.puffin.enums.ScanEvent;
import com.arraywork.puffin.enums.ScanState;
import com.arraywork.springforce.SseChannel;
import com.arraywork.springforce.filewatch.FileSystemListener;
import com.arraywork.springforce.util.Times;

import jakarta.annotation.Resource;

/**
 * 媒体库监听器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/27
 */
@Component
public class LibraryListener implements FileSystemListener {

    private static List<Scanning> scanLogs = new CopyOnWriteArrayList<>();

    @Resource
    private MetadataService metadataService;
    @Resource
    private SseChannel channel;

    // 监听启动回调方法
    @Override
    public void onStarted(File file, int count, int total) {
        Times.delay(300);
        Scanning scanning = new Scanning(ScanEvent.SCAN);
        scanning.count = count;
        scanning.total = total;
        scanning.path = file.getPath();

        try {
            Metadata metadata = metadataService.build(file);
            if (metadata != null) {
                scanning.state = ScanState.SUCCESS;
            } else {
                scanning.state = ScanState.SKIPPED;
            }
        } catch (Exception e) {
            scanning.state = ScanState.FAILED;
            scanning.message = e.getMessage();
        } finally {
            scanLogs.add(scanning);
            channel.broadcast(scanning);

            if (count == total) {
                scanning = new Scanning(ScanEvent.SCAN);
                scanning.total = total;
                scanning.state = ScanState.FINISHED;
                scanning.message = "本次扫描共发现" + scanning.total + "个文件。成功" + 0 + "个，跳过" + 0 + "个，失败" + 0 + "个。";
                scanLogs.add(scanning);
                channel.broadcast(scanning);
            }
        }
    }

    // 新增文件回调方法
    @Override
    public void onAdded(File file, int count, int total) {
        Times.delay(300);
        Metadata metadata = metadataService.build(file);
    }

    // 修改文件回调方法
    @Override
    public void onModified(File file, int count, int total) {
        Times.delay(300);
        Metadata metadata = metadataService.build(file);
    }

    // 删除文件回调方法
    @Override
    public void onDeleted(File file, int count, int total) {
        Times.delay(300);
        Metadata metadata = metadataService.delete(file);
    }

}