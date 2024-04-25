package com.arraywork.puffin.service;

import java.io.File;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.repo.PreferenceRepo;
import com.arraywork.springfield.external.BCryptEncoder;
import com.arraywork.springfield.filewatch.DirectoryWatcher;
import com.arraywork.springfield.util.Assert;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;

/**
 * 偏好服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
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
    public Preference getPreference() {
        Optional<Preference> optional = preferenceRepo.findById(Long.MAX_VALUE);
        return optional.orElse(null);
    }

    // 保存偏好
    @Transactional(rollbackFor = Exception.class)
    public Preference save(Preference preference) {
        preference.setPassword(bCryptEncoder.encode(preference.getPassword()));

        // 检查路径是否存在且为目录
        String library = preference.getLibrary();
        File file = new File(library);
        Assert.isTrue(file.exists(), "媒体库路径不存在");
        Assert.isTrue(file.isDirectory(), "媒体库路径必须为目录");

        // 监听媒体库目录
        watcher.start(library);

        return preferenceRepo.save(preference);
    }

    @PreDestroy
    public void onDestroyed() {
        watcher.stop();
    }

}