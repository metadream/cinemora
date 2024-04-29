package com.arraywork.puffin.service;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.MediaInfo;
import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.entity.VideoInfo;
import com.arraywork.puffin.enums.Quality;
import com.arraywork.puffin.repo.MetadataRepo;
import com.arraywork.puffin.spec.MetadataSpec;
import com.arraywork.springforce.util.Assert;
import com.arraywork.springforce.util.Files;
import com.arraywork.springforce.util.KeyGenerator;
import com.arraywork.springforce.util.Pagination;

import jakarta.annotation.Resource;

/**
 * 元数据服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class MetadataService {

    @Resource
    private FfmpegService ffmpegService;
    @Resource
    @Lazy
    private PreferenceService prefsService;
    @Resource
    private MetadataRepo metadataRepo;

    @Value("${puffin.page-size}")
    private int pageSize;

    @Value("${puffin.cover.base-dir}")
    private String coverBaseDir;

    // 查询分页元数据
    public Pagination<Metadata> getMetadatas(String page, Metadata condition) {
        page = page != null && page.matches("\\d+") ? page : "1";
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize);
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

    // 根据文件创建元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata create(File file) {
        MediaInfo mediaInfo = ffmpegService.getMediaInfo(file);
        Assert.isTrue(mediaInfo != null && mediaInfo.getVideo() != null, "无法提取视频元数据");

        Metadata metadata = new Metadata();
        VideoInfo video = mediaInfo.getVideo();
        metadata.setMediaInfo(mediaInfo);
        metadata.setQuality(adaptQuality(video.getWidth(), video.getHeight()));
        metadata.setTitle(Files.getName(file.getName()));
        metadata.setFilePath(file.getPath());
        metadata.setFileSize(file.length());
        metadata.setCode(KeyGenerator.nanoId(9, "0123456789"));
        metadataRepo.save(metadata); // 先保存以便截图获取ID

        // 视频截图
        String coverName = metadata.getId() + ".jpg";
        File coverFile = Path.of(coverBaseDir, coverName).toFile();
        ffmpegService.screenshot(file, coverFile, mediaInfo.getDuration() / 2);
        return metadata;
    }

    // 保存元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata save(Metadata metadata) {
        Metadata _metadata = metadataRepo.getReferenceById(metadata.getId());
        metadata.setCode(metadata.getCode().toUpperCase());
        metadata.setFilePath(_metadata.getFilePath());
        metadata.setFileSize(_metadata.getFileSize());
        metadata.setMediaInfo(_metadata.getMediaInfo());

        // TODO 上传封面
        return metadataRepo.save(metadata);
    }

    // 删除元数据
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, boolean autoDeleteFile) {
        Metadata metadata = metadataRepo.findById(id).orElse(null);
        Assert.notNull(metadata, "记录不存在或已被删除");
        metadataRepo.delete(metadata);

        // TODO 删除封面

        // 删除文件
        if (autoDeleteFile) {
            File file = new File(metadata.getFilePath());
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    // 清空路径对应文件不存在的元数据
    @Transactional(rollbackFor = Exception.class)
    public void purge(String library) {
        List<Metadata> metadatas = metadataRepo.findAll();
        Iterator<Metadata> iterator = metadatas.iterator();

        // 此方式迭代可正确删除集合中的元素
        while (iterator.hasNext()) {
            Metadata metadata = iterator.next();
            File file = new File(metadata.getFilePath());

            // 如果原始文件不存在、或者不是媒体库下的文件则删除
            if (!file.exists() || !file.getParent().equals(library)) {
                metadataRepo.delete(metadata);

                // 删除封面文件
                String coverName = metadata.getId() + ".jpg";
                File coverFile = Path.of(coverBaseDir, coverName).toFile();
                if (coverFile.exists()) coverFile.delete();
            }
        }
    }

    private Quality adaptQuality(int width, int height) {
        if (height >= width) return Quality.XX;
        if (height > 4000) return Quality.EK;   // 8192×4320
        if (height > 2000) return Quality.FK;   // 4096×2160
        if (height > 1000) return Quality.FHD;  // 1920×1080
        if (height > 700) return Quality.HD;    // 1280×720
        if (height > 400) return Quality.SD;    // 640×480
        return Quality.XX;
    }

}