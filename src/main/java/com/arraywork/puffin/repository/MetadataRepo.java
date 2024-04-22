package com.arraywork.puffin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraywork.puffin.entity.Metadata;

/**
 * Metadata Repository
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/02/21
 */
public interface MetadataRepo extends JpaRepository<Metadata, Long> {}