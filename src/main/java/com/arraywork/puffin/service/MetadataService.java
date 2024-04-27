package com.arraywork.puffin.service;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.MediaInfo;
import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.repo.MetadataRepo;
import com.arraywork.puffin.spec.MetadataSpec;
import com.arraywork.springforce.util.Assert;
import com.arraywork.springforce.util.Digest;
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

    @Value("${puffin.cover-dir}")
    private String coverDir;

    // 查询分页元数据
    public Pagination<Metadata> getMetadatas(String page, Metadata condition) {
        page = page != null && page.matches("\\d+") ? page : "1";
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize);
        Page<Metadata> pageInfo = metadataRepo.findAll(new MetadataSpec(condition), pageable);
        return new Pagination<Metadata>(pageInfo);
    }

    // 根据文件创建元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata create(File file) {
        MediaInfo mediaInfo = ffmpegService.getMediaInfo(file);
        Assert.isTrue(mediaInfo != null && mediaInfo.getVideo() != null, "无法提取视频元数据");

        Metadata metadata = new Metadata();
        metadata.setTitle(file.getName());
        metadata.setFilePath(file.getPath());
        metadata.setFileSize(file.length());
        metadata.setMediaInfo(mediaInfo);

        // 自动生成编号
        if (prefsService.getPreference().isAutoGenerateCode()) {
            metadata.setCode(Digest.nanoId(9)); // TODO 全数字id
        }
        // 先保存以便设置ID
        metadataRepo.save(metadata);

        // 视频截图
        String coverUrl = coverDir + "/" + metadata.getId() + ".jpg";
        ffmpegService.screenshot(file, new File(coverUrl), mediaInfo.getDuration() / 2);
        metadata.setCoverUrl(coverUrl);
        return metadata;
    }

    // 保存元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata save(Metadata metadata) {
        Metadata _metadata = metadataRepo.getReferenceById(metadata.getId());
        metadata.setFilePath(_metadata.getFilePath()); // TODO 测试是否需要重复设置
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
                File coverFile = new File(metadata.getCoverUrl());
                if (coverFile.exists()) coverFile.delete();
            }
        }
    }

}