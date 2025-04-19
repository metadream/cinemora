package com.arraywork.cinemora.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arraywork.cinemora.entity.Metadata;

/**
 * 元数据持久化
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/02/21
 */
public interface MetadataRepo extends JpaRepository<Metadata, String>, JpaSpecificationExecutor<Metadata> {

    Metadata findByCode(String code);
    Metadata findByFilePath(String filePath);

}