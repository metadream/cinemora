package com.arraywork.puffin.service;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.arraywork.puffin.entity.Library;
import com.arraywork.puffin.entity.MediaInfo;
import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.entity.ScanStatus;
import com.arraywork.puffin.repo.LibraryRepo;
import com.arraywork.puffin.repo.MetadataRepo;
import com.arraywork.springhood.util.CommonUtils;

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
    private LibraryRepo libraryRepo;
    @Resource
    private MetadataRepo metadataRepo;

    @Value("${puffin.folder.cover}")
    private String coverFolder;

    // 获取媒体库列表
    public List<Library> getLibraries() {
        return libraryRepo.findAll();
    }

    // 保存媒体库
    @Transactional(rollbackFor = Exception.class)
    public Library save(Library library) {
        return libraryRepo.save(library);
    }

    // 删除媒体库
    @Transactional(rollbackFor = Exception.class)
    public void deleteLibrary(String id) {
        Library library = libraryRepo.getReferenceById(id);
        libraryRepo.delete(library); // TODO 联级删除元数据测试
    }

    // 扫描所有媒体库
    public void scanAll() {
        if (!SCAN_STATUS.completed) return;
        SCAN_STATUS.reset();

        for (Library library : getLibraries()) {
            purgeLibrary(library);
            scanLibrary(library);
            generateCovers(library);
        }
    }

    // 扫描单个媒体库
    public void scanById(String id) {
        if (!SCAN_STATUS.completed) return;
        SCAN_STATUS.reset();

        // TODO 测试是否需要Asset.notnull
        Library library = libraryRepo.getReferenceById(id);
        purgeLibrary(library);
        scanLibrary(library);
        generateCovers(library);
    }

    // 清除媒体库中路径对应实体文件不存在的元数据、封面
    private void purgeLibrary(Library library) {
        for (Metadata metadata : library.getMetadatas()) {
            File mediaFile = new File(metadata.getPath());
            if (!mediaFile.exists()) {
                metadataRepo.delete(metadata);
                File coverFile = Path.of(coverFolder, String.valueOf(metadata.getId())).toFile();
                if (coverFile.exists()) coverFile.delete();
            }
        }
    }

    // 扫描媒体库获取元数据
    private void scanLibrary(Library library) {
        File libraryDir = new File(library.getPath());
        List<File> mediaFiles = new ArrayList<>();
        CommonUtils.walkDir(libraryDir, mediaFiles);

        List<Metadata> metadatas = library.getMetadatas();
        for (File mediaFile : mediaFiles) {
            SCAN_STATUS.totalFiles++;
            String filename = mediaFile.getName();

            if (isVideoFile(filename)) {
                LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(mediaFile.lastModified()),
                    ZoneId.systemDefault());
                Metadata metadata = new Metadata();
                metadata.setTitle(filename.replaceAll("\\.[^\\.]+$", ""));
                metadata.setPath(mediaFile.getPath());
                metadata.setVideoSize(mediaFile.length());
                metadata.setLastModified(lastModified);
                metadatas.add(metadata);

                SCAN_STATUS.totalMedias++;
                // TODO 事务处理
            }
        }
    }

    // 截取视频帧生成封面
    private void generateCovers(Library library) {
        List<Metadata> metadatas = library.getMetadatas();
        for (Metadata metadata : metadatas) {
            File coverFile = Path.of(coverFolder, String.valueOf(metadata.getId())).toFile();
            if (coverFile.exists()) continue;

            // 更新媒体信息
            MediaInfo mediaInfo = ffmpeg.getMediaInfo(metadata.getPath());
            if (mediaInfo.getDuration() > 0 && mediaInfo.getVideo() != null) {
                metadata.setDuration(mediaInfo.getDuration());
                metadata.setVideoWidth(mediaInfo.getVideo().getWidth());
                metadata.setVideoHeight(mediaInfo.getVideo().getHeight());
                // this.movieRepo.update(movie);
            }

            // 截取封面
            ffmpeg.screenshot(metadata.getPath(), coverFile.getPath(), metadata.getDuration());
            CommonUtils.delay(100);
            SCAN_STATUS.processed++;
        }
    }

    // 判断文件名是否属于视频
    private boolean isVideoFile(String filename) {
        String extname = StringUtils.getFilenameExtension(filename);
        return Arrays.asList(VIDEO_FORMATS).contains(extname);
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.getFilenameExtension(".3gp"));
    }

    // @PostConstruct
    // public void test() {
    // System.out.println("--------------------");
    // Metadata metadata = new Metadata();
    //
    // metadata.setCode("asdfs");
    // metadata.setTitle("警方撒旦卡了发动机是");
    // metadata.setPath("/dfad/s/fdsafds");
    // metadata.setQuality(Quality.HD);
    //
    // TODO json array query
    // String[] aa = { "aa", "bbb", "ccc", "dddddddddddddddddd" };
    // metadata.setProducers(aa);
    // metadataRepo.save(metadata);
    //
    // List<Metadata> list = metadataRepo.findAll(new MetadataSpec());
    // System.out.println(list);
    // }

}