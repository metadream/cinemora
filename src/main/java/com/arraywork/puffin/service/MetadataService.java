package com.arraywork.puffin.service;

import java.io.File;

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
    @Resource @Lazy
    private PreferenceService prefsService;
    @Resource
    private MetadataRepo metadataRepo;

    @Value("${puffin.page-size}")
    private int pageSize;

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

        if (mediaInfo != null && mediaInfo.getVideo() != null) {
            Metadata metadata = new Metadata();
            metadata.setTitle(file.getName());
            metadata.setFilePath(file.getPath());
            metadata.setFileSize(file.length());
            metadata.setMediaInfo(mediaInfo);

            try {
                boolean autoGenerateCode = prefsService.getPreference().isAutoGenerateCode();
                if (autoGenerateCode) {
                    metadata.setCode(Digest.nanoId(9)); // TODO 全数字id
                }
                if (file.getName().equals("333.avi")) {
                    int a = 1 / 0;
                }
                return metadataRepo.save(metadata);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
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

}