package com.arraywork.cinemora.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraywork.cinemora.entity.Settings;

/**
 * 偏好持久化
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/02/21
 */
public interface SettingRepo extends JpaRepository<Settings, Long> { }