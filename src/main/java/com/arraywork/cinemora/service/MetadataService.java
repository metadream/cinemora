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

import com.arraywork.autumn.channel.SseChannel;
import com.arraywork.autumn.id.KeyGenerator;
import com.arraywork.autumn.util.Assert;
import com.arraywork.autumn.util.FileUtils;
import com.arraywork.autumn.util.Pagination;
import com.arraywork.autumn.util.TimeUtils;
import com.arraywork.cinemora.entity.MediaInfo;
import com.arraywork.cinemora.entity.Metadata;
import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.entity.VideoInfo;
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
    private SseChannel channel;
    @Resource
    private MetadataRepo metadataRepo;

    @Value("${app.page-size}")
    private int pageSize;

    @Value("${app.covers}")
    private String coverBaseDir;

    // 查询分页元数据
    public Pagination<Metadata> getMetadata(String page, Metadata condition) {
        Sort sort = Sort.by("lastModified").descending().and(Sort.by("code").descending());
        page = page != null && page.matches("\\d+") ? page : "1";
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize, sort);
        Page<Metadata> pageInfo = metadataRepo.findAll(new MetadataSpec(condition), pageable);
        return new Pagination<Metadata>(pageInfo);
    }

    // 根据ID获取元数据
    public Metadata getById(String id) {
        Optional<Metadata> optional = metadataRepo.findById(id);
        Assert.isTrue(optional.isPresent(), HttpStatus.NOT_FOUND);
        return optional.get();
    }

    // 根据编号获取元数据
    public Metadata getByCode(String code) {
        Metadata metadata = metadataRepo.findByCode(code.toUpperCase());
        Assert.notNull(metadata, HttpStatus.NOT_FOUND);
        return metadata;
    }

    // 根据文件构建元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata build(File file, boolean forceReindexing) {
        // TODO 先判断数据库是否存在再提取元数据
        MediaInfo mediaInfo = ffmpegService.extract(file);
        Assert.notNull(mediaInfo, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        Assert.notNull(mediaInfo.getVideo(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);

        Path library = settingService.getLibrary();
        String relativePath = library.relativize(file.toPath()).toString();

        Metadata metadata = metadataRepo.findByFilePath(relativePath);
        if (metadata == null) {
            metadata = new Metadata();
            metadata.setCode(KeyGenerator.nanoId(9, "0123456789"));
            forceReindexing = true;
        }
        if (forceReindexing) {
            // 保存元数据
            VideoInfo video = mediaInfo.getVideo();
            metadata.setMediaInfo(mediaInfo);
            metadata.setQuality(adaptQuality(video.getWidth(), video.getHeight()));
            metadata.setTitle(FileUtils.getName(file.getName()));
            metadata.setFilePath(relativePath);
            metadata.setFileSize(file.length());
            metadata.setLastModified(TimeUtils.toLocal(file.lastModified()));
            metadataRepo.save(metadata); // 先保存以便获取ID供截图使用

            // 创建缩略图
            File coverFile = getCoverPath(metadata.getId()).toFile();
            ffmpegService.screenshot(file, coverFile, mediaInfo.getDuration() / 2);
            //        OpenCv.captureVideo(file.getPath(), coverFile.getPath(), 1920);
            return metadata;
        }

        // 如果封面不存在、或者存在但需强制重建，则进行视频截图
        //        OpenCv.captureVideo(file.getPath(), coverFile.getPath(), 1920);

        //        boolean coverExists = coverFile.exists();
        //        if (!coverExists || (coverExists && forceReindexingCover)) {
        //            metadata.setMediaInfo(mediaInfo);
        //            metadata.setQuality(adaptQuality(video.getWidth(), video.getHeight()));
        //            metadata.setFileSize(file.length());
        //            ffmpegService.screenshot(file, coverFile, mediaInfo.getDuration() / 2);
        //        }
        return null;
    }

    // 根据文件删除元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata delete(File file) {
        Metadata metadata = metadataRepo.findByFilePath(file.getPath());
        return delete(metadata);
    }

    // 保存元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata save(Metadata metadata) {
        String code = metadata.getCode().toUpperCase();
        Metadata _metadata = metadataRepo.findByCode(code);
        Assert.isTrue(_metadata == null || _metadata.getId().equals(metadata.getId()), "元数据编号冲突");

        if (_metadata == null) {
            _metadata = metadataRepo.getReferenceById(metadata.getId());
        }
        metadata.setCode(code);
        metadata.setFilePath(_metadata.getFilePath());
        metadata.setFileSize(_metadata.getFileSize());
        metadata.setLastModified(_metadata.getLastModified());
        metadata.setMediaInfo(_metadata.getMediaInfo());

        // 如果未手动设置画质则采用自动匹配的画质
        if (metadata.getQuality() == null) {
            metadata.setQuality(_metadata.getQuality());
        }
        // 重命名文件
        Settings settings = settingService.getSettings();
        if (settings.isAutoRename()) {
            String library = settings.getLibrary();
            String filePath = metadata.getFilePath();
            String extension = FileUtils.getExtension(filePath);
            String newName = "[" + code + "] " + metadata.getTitle() + extension;

            File oldFile = Path.of(library, filePath).toFile();
            File newFile = Path.of(oldFile.getParent(), newName).toFile();
            Assert.isTrue(oldFile.exists(), "The original file does not exist");
            Assert.isTrue(oldFile.renameTo(newFile), "File renaming failed: possibly due to a name that is too long or contains reserved characters.");
            metadata.setFilePath(newFile.getPath().substring(library.length()));
        }
        tagCloudService.clearCache();
        return metadataRepo.save(metadata);
    }

    // 上传封面图片
    public String upload(String id, MultipartFile multipartFile) throws IOException {
        Metadata metadata = getById(id);
        Path coverPath = Path.of(coverBaseDir, metadata.getId() + ".jpg");
        multipartFile.transferTo(coverPath);
        return coverPath.toString();
    }

    // 清理元数据
    @Transactional(rollbackFor = Exception.class)
    public int clean(String library) {
        List<Metadata> metadatas = metadataRepo.findAll();
        List<Metadata> toDelete = new ArrayList<>();

        for (Metadata metadata : metadatas) {
            Path filePath = Path.of(library, metadata.getFilePath());
            // 如果原始文件不存在则删除
            if (!filePath.toFile().exists()) {
                toDelete.add(metadata);
            }
        }

        int count = 0, total = toDelete.size();
        for (Metadata metadata : toDelete) {
            delete(metadata);
            count++;

            //            ScanningInfo info = new ScanningInfo(EventSource.PURGE);
            //            info.count = count;
            //            info.total = total;
            //            info.path = metadata.getFilePath();
            //            info.state = EventState.SUCCESS;
            //            channel.broadcast(info);
        }

        //        ScanningInfo info = new ScanningInfo(EventSource.PURGE);
        //        info.state = EventState.FINISHED;
        //        info.message = "本次操作共清除元数据记录" + total + "条。";
        //        channel.broadcast(info);
        return total;
    }

    // 删除元数据
    @Transactional(rollbackFor = Exception.class)
    private Metadata delete(Metadata metadata) {
        if (metadata != null) {
            metadataRepo.delete(metadata); // 删除元数据
            File coverFile = getCoverPath(metadata.getId()).toFile();
            if (coverFile.exists()) coverFile.delete(); // 删除封面图片
        }
        return metadata;
    }

    // 根据ID获取封面路径
    private Path getCoverPath(String id) {
        return Path.of(coverBaseDir, id + ".jpg");
    }

    // 根据分辨率适配画质 // TODO 考虑竖屏不能简单用height >= width判断
    private Quality adaptQuality(int width, int height) {
        if (height >= width) return Quality.LD;
        if (height > 4000) return Quality.EK;   // 8192×4320
        if (height > 2000) return Quality.FK;   // 4096×2160
        if (height > 1000) return Quality.FHD;  // 1920×1080
        if (height > 700) return Quality.HD;    // 1280×720
        if (height > 400) return Quality.SD;    // 640×480
        return Quality.LD;
    }

}