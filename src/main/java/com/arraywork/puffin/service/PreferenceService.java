package com.arraywork.puffin.service;

import java.io.File;
import java.util.Optional;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.repo.PreferenceRepo;
import com.arraywork.springforce.external.BCryptEncoder;
import com.arraywork.springforce.filewatch.DirectoryWatcher;
import com.arraywork.springforce.util.Assert;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;

/**
 * 偏好服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
@EnableCaching
public class PreferenceService {

    @Resource
    private BCryptEncoder bCryptEncoder;
    @Resource
    private DirectoryWatcher watcher;
    @Resource
    private PreferenceRepo preferenceRepo;

    // 登录
    public boolean login(Preference user) {
        return false;
    }

    // 获取偏好
    @Cacheable(value = "preference", key = "'#preference'")
    public Preference getPreference() {
        Optional<Preference> optional = preferenceRepo.findById(Long.MAX_VALUE);
        return optional.orElse(null);
    }

    // 保存偏好
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "preference", key = "'#preference'")
    public Preference save(Preference entity) {
        // 检查路径是否存在且为目录
        String library = entity.getLibrary();
        File entry = new File(library);
        Assert.isTrue(entry.exists(), "媒体库路径不存在");
        Assert.isTrue(entry.isDirectory(), "媒体库路径必须为目录");

        // 修改密码
        Preference preference = getPreference();
        if ("********".equals(entity.getPassword())) {
            entity.setPassword(preference.getPassword());
        } else {
            entity.setPassword(bCryptEncoder.encode(entity.getPassword()));
        }

        // 监听媒体库目录
        if (!library.equals(preference.getLibrary())) {
            watcher.start(library);
        }
        return preferenceRepo.save(entity);
    }

    @PreDestroy
    public void onDestroyed() {
        watcher.stop();
    }

}