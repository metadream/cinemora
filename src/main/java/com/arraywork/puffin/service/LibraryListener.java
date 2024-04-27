package com.arraywork.puffin.service;

import java.io.File;

import org.springframework.stereotype.Component;

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

    @Resource
    private MetadataService metadataService;

    @Override
    public void onStarted(File file, int count, int total) {
        System.out.print("Started[" + count + "/" + total + "]: ");
        System.out.println(file);

        try {
            CommonUtils.delay(500);
            metadataService.create(file);
        } catch (Exception e) {
            System.out.println("error ignored: " + e.getMessage());
        }
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