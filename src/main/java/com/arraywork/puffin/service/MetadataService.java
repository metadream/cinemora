package com.arraywork.puffin.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.repo.MetadataRepo;
import com.arraywork.puffin.spec.MetadataSpec;
import com.arraywork.springfield.util.Assert;
import com.arraywork.springfield.util.Pagination;

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
    private MetadataRepo metadataRepo;

    @Value("${puffin.page-size}")
    protected int pageSize;

    // @PostConstruct
    public void init() {
        Metadata metadata = new Metadata();
        metadata.setCode("3333");
        metadata.setTitle("阿斯捷克洛夫的萨");
        metadata.setFilepath("/fdsjakl/fdsafads");
        metadataRepo.save(metadata);
    }

    // 获取所有元数据
    public List<Metadata> getMetadatas() {
        return metadataRepo.findAll();
    }

    // 查询分页元数据
    public Pagination<Metadata> getMetadatas(String page, Metadata condition) {
        page = page != null && page.matches("\\d+") ? page : "1";
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize);
        Page<Metadata> pageInfo = metadataRepo.findAll(new MetadataSpec(condition), pageable);
        return new Pagination<Metadata>(pageInfo);
    }

    // 保存元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata save(Metadata entity) {
        Metadata metadata = metadataRepo.getReferenceById(entity.getId());
        entity.setFilepath(metadata.getFilepath());
        entity.setMediaInfo(metadata.getMediaInfo());

        // TODO 上传封面
        return metadataRepo.save(entity);
    }

    // 删除元数据
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, boolean syncDeleteFile) {
        Metadata metadata = metadataRepo.findById(id).orElse(null);
        Assert.notNull(metadata, "记录不存在或已被删除");
        metadataRepo.delete(metadata);

        // 同时删除媒体文件
        if (syncDeleteFile) {
            File file = new File(metadata.getFilepath());
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

}