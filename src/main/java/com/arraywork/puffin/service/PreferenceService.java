package com.arraywork.puffin.service;

import java.io.File;
import java.util.Optional;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.repo.PreferenceRepo;
import com.arraywork.springforce.external.BCryptEncoder;
import com.arraywork.springforce.util.Assert;

import jakarta.annotation.Resource;

/**
 * 偏好设置服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class PreferenceService {

    @Resource
    private BCryptEncoder bCryptEncoder;
    @Resource
    private LibraryService libraryService;
    @Resource
    private PreferenceRepo prefsRepo;

    // 登录
    public boolean login(Preference user) {
        return false;
    }

    // 获取偏好
    @Cacheable(value = "preference", key = "#Long.MAX_VALUE") // TODO 是否有效
    public Preference getPreference() {
        Optional<Preference> optional = prefsRepo.findById(Long.MAX_VALUE);
        return optional.orElse(null);
    }

    // 初始化偏好
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "preference", key = "'#preference'")
    public Preference init(Preference prefs) {
        String library = prefs.getLibrary();
        checkLibraryPath(library);

        prefs.setPassword(bCryptEncoder.encode(prefs.getPassword()));
        libraryService.scan(library);  // TODO 先保存or先扫描 的区别
        return prefsRepo.save(prefs);
    }

    // 保存偏好
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "preference", key = "'#preference'")
    public Preference save(Preference prefs) {
        String library = prefs.getLibrary();
        checkLibraryPath(library);

        // 修改密码
        Preference _prefs = getPreference();
        if ("********".equals(prefs.getPassword())) { // TODO 密码传null是否会保持原密码
            prefs.setPassword(_prefs.getPassword());
        } else {
            prefs.setPassword(bCryptEncoder.encode(prefs.getPassword()));
        }

        // 变更监听目录
        if (!library.equals(_prefs.getLibrary())) {
            libraryService.scan(library);
        }
        return prefsRepo.save(prefs);
    }

    // 校验媒体库路径
    private void checkLibraryPath(String path) {
        File entry = new File(path);
        Assert.isTrue(entry.exists(), "媒体库路径不存在");
        Assert.isTrue(entry.isDirectory(), "媒体库路径必须为目录");
    }

}