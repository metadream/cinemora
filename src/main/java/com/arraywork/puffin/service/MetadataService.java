package com.arraywork.puffin.service;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Library;
import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.repo.LibraryRepo;
import com.arraywork.puffin.repo.MetadataRepo;
import com.arraywork.springhood.util.Assert;

import jakarta.annotation.Resource;

/**
 * 元数据服务
 * @author AiChen
 * @created 2024/04/22
 */
@Service
public class MetadataService {

    @Resource
    private LibraryRepo libraryRepo;
    @Resource
    private MetadataRepo metadataRepo;

    // 保存元数据
    @Transactional(rollbackFor = Exception.class)
    public Metadata saveMetadata(long libId, Metadata metadata) {
        Library library = libraryRepo.getReferenceById(libId);
        library.getMetadatas().add(metadata);
        return metadata;
    }

    // 删除元数据
    @Transactional(rollbackFor = Exception.class)
    public void deleteMetadata(long metaId, boolean deleteMediaFile) {
        Metadata metadata = metadataRepo.getReferenceById(metaId);
        Assert.notNull(metadata, "记录不存在或已被删除");
        metadataRepo.delete(metadata);

        // 同时删除媒体文件
        if (deleteMediaFile) {
            File file = new File(metadata.getPath());
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

}