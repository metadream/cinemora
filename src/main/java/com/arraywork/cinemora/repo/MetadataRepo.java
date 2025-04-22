package com.arraywork.cinemora.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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

    @Query("select m from Metadata m where " +
        " m.producers is not null or m.directors is not null or m.starring is not null " +
        " or m.series is not null or m.genres is not null or m.tags is not null")
    List<Metadata> findTags();

}