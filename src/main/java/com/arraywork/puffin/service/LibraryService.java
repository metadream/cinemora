package com.arraywork.puffin.service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.arraywork.puffin.entity.MediaInfo;
import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.entity.ScanStatus;
import com.arraywork.springfield.util.CommonUtils;

import jakarta.annotation.Resource;

/**
 * 媒体库服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class LibraryService {

    private static final ScanStatus SCAN_STATUS = new ScanStatus();
    private static final String[] VIDEO_FORMATS = { "3g2", "3gp", "asf", "avi", "dat", "flv", "m2ts", "m4v",
        "mkv", "mod", "mov", "mp4", "mpeg", "mpg", "mts", "oga", "ogg", "ogv", "qt", "rm", "rmvb", "ts", "vob",
        "webm", "wmv" };

    @Resource
    private FfmpegService ffmpeg;
    @Resource
    private PreferenceService preferenceService;
    @Resource
    private MetadataService metadataService;

    @Value("${puffin.folder.cover}")
    private String coverFolder;

    // 扫描媒体库
    @Transactional(rollbackFor = Exception.class)
    public void scan() {
        if (!SCAN_STATUS.completed) return;
        SCAN_STATUS.reset();

        List<Metadata> metadatas = metadataService.getMetadatas();
        Preference preference = preferenceService.getPreference();
        String library = preference.getLibrary();

        purge(library, metadatas);
        scan(library, metadatas);
        generateCovers(library, metadatas);
    }

    // 清理路径对应实体文件不存在的元数据、封面
    @Transactional(rollbackFor = Exception.class)
    private void purge(String library, List<Metadata> metadatas) {
        // 此方式迭代可正确删除集合中的元素
        Iterator<Metadata> iterator = metadatas.iterator();
        while (iterator.hasNext()) {
            Metadata metadata = iterator.next();
            String id = metadata.getId();
            String filepath = metadata.getFilepath();

            // 如果不以媒体库路径开头，说明媒体库路径已更改
            if (!filepath.startsWith(library)) {
                metadatas.remove(metadata);
                SCAN_STATUS.deleted++;
            } else {
                File file = new File(filepath);
                if (!file.exists()) { // 如果原始文件不存在
                    metadatas.remove(metadata);
                    SCAN_STATUS.deleted++;

                    // 删除封面文件
                    File coverFile = Path.of(coverFolder, id).toFile();
                    if (coverFile.exists()) coverFile.delete();
                }
            }
        }
    }

    // 扫描媒体库以获取或更新元数据
    @Transactional(rollbackFor = Exception.class)
    private void scan(String library, List<Metadata> metadatas) {
        List<File> files = new ArrayList<>();
        File libraryDir = new File(library);
        CommonUtils.walkDir(libraryDir, files);

        for (File file : files) {
            String filename = file.getName();
            if (isVideoFile(filename)) {
                // TODO 如何避免重复新增？
                Metadata metadata = new Metadata();
                metadata.setTitle(filename.replaceAll("\\.[^\\.]+$", ""));
                metadata.setLastModified(CommonUtils.toLocalDateTime(file.lastModified()));
                metadata.setFilepath(file.getPath());
                metadatas.add(metadata);

                SCAN_STATUS.totalMedias++;
                SCAN_STATUS.inserted++;
            }
            SCAN_STATUS.totalFiles++;
        }
    }

    // 截取视频帧生成封面并更新媒体信息
    @Transactional(rollbackFor = Exception.class)
    private void generateCovers(String library, List<Metadata> metadatas) {
        for (Metadata metadata : metadatas) {
            File coverFile = Path.of(coverFolder, metadata.getId()).toFile();
            if (coverFile.exists()) continue;

            // 更新媒体信息
            String filepath = metadata.getFilepath();
            MediaInfo mediaInfo = ffmpeg.getMediaInfo(filepath);
            if (mediaInfo.getDuration() > 0 && mediaInfo.getVideo() != null) {
                metadata.setMediaInfo(mediaInfo);
            }

            // 截取封面
            ffmpeg.screenshot(filepath, coverFile.getPath(), mediaInfo.getDuration());
            CommonUtils.delay(100);
            SCAN_STATUS.processed++;
        }
    }

    // 判断文件名是否属于视频
    private boolean isVideoFile(String filename) {
        String extname = StringUtils.getFilenameExtension(filename);
        return Arrays.asList(VIDEO_FORMATS).contains(extname);
    }

}