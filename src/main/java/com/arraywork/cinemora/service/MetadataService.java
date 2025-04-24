package com.arraywork.cinemora.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.arraywork.autumn.id.KeyGenerator;
import com.arraywork.autumn.util.Assert;
import com.arraywork.autumn.util.FileUtils;
import com.arraywork.autumn.util.Pagination;
import com.arraywork.cinemora.entity.MediaInfo;
import com.arraywork.cinemora.entity.Metadata;
import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.entity.VideoInfo;
import com.arraywork.cinemora.enums.EventState;
import com.arraywork.cinemora.enums.Quality;
import com.arraywork.cinemora.repo.MetadataRepo;
import com.arraywork.cinemora.repo.MetadataSpec;

/**
 * 元数据服务
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class MetadataService {

    @Resource
    @Lazy
    private SettingService settingService;
    @Resource
    private FfmpegService ffmpegService;
    @Resource
    private TagCloudService tagCloudService;
    @Resource
    private MetadataRepo metadataRepo;

    @Value("${app.page-size}")
    private int pageSize;

    @Value("${app.covers}")
    private String coverDir;

    /** 查询分页元数据 */
    public Pagination<Metadata> getMetadata(String page, Metadata condition) {
        Sort sort = Sort.by("lastModified").descending().and(Sort.by("code").descending());
        page = page != null && page.matches("\\d+") ? page : "1";
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize, sort);
        Page<Metadata> pageInfo = metadataRepo.findAll(new MetadataSpec(condition), pageable);
        return new Pagination<>(pageInfo);
    }

    /** 查询无效的元数据 */
    public List<Metadata> getOrphanedMetadata() {
        Path library = settingService.getLibrary();
        List<Metadata> allList = metadataRepo.findAll();
        List<Metadata> orphanedList = new ArrayList<>();

        for (Metadata metadata : allList) {
            Path filePath = library.resolve(metadata.getFilePath());
            if (!filePath.toFile().exists()) {
                orphanedList.add(metadata);
            }
        }
        return orphanedList;
    }

    /** 根据ID获取元数据 */
    public Metadata getById(String id) {
        Optional<Metadata> optional = metadataRepo.findById(id);
        Assert.isTrue(optional.isPresent(), HttpStatus.NOT_FOUND);
        return optional.get();
    }

    /** 根据编号获取元数据 */
    public Metadata getByCode(String code) {
        Metadata metadata = metadataRepo.findByCode(code.toUpperCase());
        Assert.notNull(metadata, HttpStatus.NOT_FOUND);
        return metadata;
    }

    /** 根据文件构建元数据 */
    @Transactional(rollbackFor = Exception.class)
    public EventState build(File file, boolean forceReindexing) {
        Path library = settingService.getLibrary();
        String relativePath = library.relativize(file.toPath()).toString();
        Metadata metadata = metadataRepo.findByFilePath(relativePath);
        EventState state;

        // 设置返回值
        if (metadata != null) {
            state = forceReindexing ? EventState.REINDEXED : EventState.SKIPPED;
        } else {
            metadata = new Metadata();
            metadata.setCode(KeyGenerator.nanoId(9, "0123456789"));
            state = EventState.INDEXED;
            forceReindexing = true;  // 如果元数据不存在则无论是否强制都建立索引
        }

        // 提取元数据
        if (forceReindexing) {
            MediaInfo mediaInfo = ffmpegService.extract(file);
            Assert.notNull(mediaInfo, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            Assert.notNull(mediaInfo.getVideo(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            if (mediaInfo == null || mediaInfo.getVideo() == null) {
                throw new IllegalArgumentException("Unsupported media");
            }

            // 保存元数据
            VideoInfo video = mediaInfo.getVideo();
            metadata.setMediaInfo(mediaInfo);
            metadata.setQuality(Quality.fromSize(video.getWidth(), video.getHeight()));
            metadata.setTitle(FileUtils.getName(file.getName()));
            metadata.setFilePath(relativePath);
            metadata.setFileSize(file.length());
            metadataRepo.save(metadata); // 先保存以便获取ID供截图使用

            // 创建缩略图（截取视频时长一半时显示的画面）
            File coverFile = resolveCoverPath(metadata.getId()).toFile();
            ffmpegService.screenshot(file, coverFile, mediaInfo.getDuration() / 2);
        }
        return state;
    }

    /** 保存元数据 */
    @Transactional(rollbackFor = Exception.class)
    public Metadata save(Metadata metadata) {
        String code = metadata.getCode().toUpperCase();
        Metadata _metadata = metadataRepo.findByCode(code);
        Assert.isTrue(_metadata == null || _metadata.getId().equals(metadata.getId()), "Duplicate metadata code detected.");

        if (_metadata == null) {
            _metadata = metadataRepo.getReferenceById(metadata.getId());
        }
        metadata.setCode(code);
        metadata.setFilePath(_metadata.getFilePath());
        metadata.setFileSize(_metadata.getFileSize());
        metadata.setMediaInfo(_metadata.getMediaInfo());

        // 如果未手动设置画质则采用自动匹配的画质
        if (metadata.getQuality() == null) {
            metadata.setQuality(_metadata.getQuality());
        }

        // 重命名文件
        Settings settings = settingService.getSettings();
        if (settings.isAutoRename()) {
            Path library = Path.of(settings.getLibrary());
            String filePath = metadata.getFilePath();
            String extension = FileUtils.getExtension(filePath);
            String newName = "[" + code + "] " + metadata.getTitle() + extension;

            File oldFile = library.resolve(filePath).toFile();
            File newFile = Path.of(oldFile.getParent(), newName).toFile();
            Assert.isTrue(oldFile.exists(), "The original file does not exist");
            Assert.isTrue(oldFile.renameTo(newFile), "File renaming failed: possibly due to a name that is too long or contains reserved characters.");
            metadata.setFilePath(library.relativize(newFile.toPath()).toString());
        }

        tagCloudService.clearCache();
        return metadataRepo.save(metadata);
    }

    /** 根据文件删除元数据 */
    @Transactional(rollbackFor = Exception.class)
    public EventState delete(File file) {
        Path library = settingService.getLibrary();
        String relativePath = library.relativize(file.toPath()).toString();
        Metadata metadata = metadataRepo.findByFilePath(relativePath);
        return delete(metadata) != null ? EventState.DELETED : EventState.SKIPPED;
    }

    /** 删除元数据 */
    @Transactional(rollbackFor = Exception.class)
    public Metadata delete(Metadata metadata) {
        if (metadata != null) {
            metadataRepo.delete(metadata); // 删除元数据
            File coverFile = resolveCoverPath(metadata.getId()).toFile();
            if (coverFile.exists()) coverFile.delete(); // 删除封面图片
        }
        return metadata;
    }

    /** 上传封面图片 */
    public String upload(String id, MultipartFile multipartFile) throws IOException {
        Metadata metadata = getById(id);
        Path coverPath = resolveCoverPath(metadata.getId());
        multipartFile.transferTo(coverPath);
        return coverPath.toString();
    }

    /** 根据ID构建封面路径 */
    public Path resolveCoverPath(String id) {
        return Path.of(coverDir, id + ".jpg");
    }

}