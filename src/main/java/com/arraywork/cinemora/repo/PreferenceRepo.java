package com.arraywork.cinemora.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraywork.cinemora.entity.Preference;

/**
 * 偏好持久化
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/02/21
 */
public interface PreferenceRepo extends JpaRepository<Preference, Long> { }