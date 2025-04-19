package com.arraywork.cinemora.service;

import java.io.File;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.arraywork.autumn.channel.SseChannel;
import com.arraywork.autumn.helper.DirectoryMonitor;
import com.arraywork.autumn.util.TimeUtils;
import com.arraywork.cinemora.entity.Metadata;
import com.arraywork.cinemora.entity.ScanningInfo;
import com.arraywork.cinemora.enums.ScanEvent;
import com.arraywork.cinemora.enums.ScanState;

import lombok.extern.slf4j.Slf4j;

/**
 * 媒体库监听器
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/27
 */
@Component
@Slf4j
public class LibraryListener extends DirectoryMonitor.DirectoryListener {

    private int success;
    private int skipped;
    private int failed;

    @Resource
    private MetadataService metadataService;
    @Resource
    private SseChannel channel;

    // 监听启动回调方法
    //    @Override
    //    public void onStart(File file, int count, int total) {
    //        onChanged(file, count, total, ScanEvent.SCAN, () -> metadataService.build(file, false));
    //    }

    // 新增文件回调方法
    @Override
    public void onFileCreate(File file) {
        //        onChanged(file, count, total, ScanEvent.ADD, () -> metadataService.build(file, false));
    }

    // 修改文件回调方法
    @Override
    public void onFileChange(File file) {
        //        onChanged(file, count, total, ScanEvent.MODIFY, () -> metadataService.build(file, true));
    }

    // 删除文件回调方法
    @Override
    public void onFileDelete(File file) {
        //        onChanged(file, count, total, ScanEvent.DELETE, () -> metadataService.delete(file));
    }

    // 监听回调方法
    private void onChanged(File file, int count, int total, ScanEvent event, Consumer consumer) {
        TimeUtils.delay(200);
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