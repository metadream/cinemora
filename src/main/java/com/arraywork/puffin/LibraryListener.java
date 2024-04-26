package com.arraywork.puffin;

import java.io.File;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.service.FfmpegService;
import com.arraywork.puffin.service.MetadataService;
import com.arraywork.puffin.service.PreferenceService;
import com.arraywork.springforce.filewatch.FileSystemListener;
import com.arraywork.springforce.util.Digest;

import jakarta.annotation.Resource;

/**
 * 媒体库监听器
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/25
 */
@Component
public class LibraryListener implements FileSystemListener {

    @Resource
    private FfmpegService ffmpegService;
    @Resource
    private PreferenceService preferenceService;
    @Resource
    private MetadataService metadataService;

    // TODO 成功/失败计数
    @Override
    public void onStarted(File file, int count, int total) {
        System.out.print("Started[" + count + "/" + total + "]: ");
        System.out.println(file);

        onAdded(file);
    }

    @Override @Transactional(rollbackFor = Exception.class)
    public void onAdded(File file) {
        System.out.print("Added: ");
        System.out.println(file);

        try {
            Metadata metadata = new Metadata();
            metadata.setTitle(file.getName());
            metadata.setFilePath(file.getPath());
            metadata.setFileSize(file.length());
            metadata.setMediaInfo(ffmpegService.getMediaInfo(file));

            boolean autoGenerateCode = preferenceService.getPreference().isAutoGenerateCode();
            if (autoGenerateCode) {
                metadata.setCode(Digest.nanoId(9));
            }
            metadataService.add(metadata);
        } catch (Exception e) {
            System.out.println("====" + e.getMessage());
        }
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