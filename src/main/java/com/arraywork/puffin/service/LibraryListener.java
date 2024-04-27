package com.arraywork.puffin.service;

import java.io.File;

import org.springframework.stereotype.Component;

import com.arraywork.puffin.entity.ScanStatus;
import com.arraywork.springforce.filewatch.FileSystemListener;
import com.arraywork.springforce.util.CommonUtils;

import jakarta.annotation.Resource;

/**
 * 媒体库监听器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/27
 */
@Component
public class LibraryListener implements FileSystemListener {

    private ScanStatus scanStatus = new ScanStatus();
    @Resource
    private MetadataService metadataService;

    @Override
    public void onStarted(File file, int count, int total) {
        scanStatus.path = file.getPath();
        scanStatus.count = count;
        scanStatus.total = total;
        scanStatus.event = "扫描";
        scanStatus.result = null;
        scanStatus.error = null;

        try {
            CommonUtils.delay(500);
            metadataService.create(file);
            scanStatus.result = "成功";
            scanStatus.success++;
        } catch (Exception e) {
            scanStatus.result = "失败";
            scanStatus.error = e.getMessage();
            scanStatus.failed++;
        }
        System.out.println(scanStatus);
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

}