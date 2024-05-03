package com.arraywork.puffin.service;

import java.io.File;
import java.util.Optional;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.repo.PreferenceRepo;
import com.arraywork.springforce.external.BCryptEncoder;
import com.arraywork.springforce.security.SecurityContext;
import com.arraywork.springforce.security.SecurityService;
import com.arraywork.springforce.util.Assert;

import jakarta.annotation.Resource;

/**
 * 偏好设置服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class PreferenceService implements SecurityService {

    @Resource
    private SecurityContext context;
    @Resource
    private BCryptEncoder bCryptEncoder;
    @Resource
    @Lazy
    private LibraryService libraryService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private PreferenceRepo prefsRepo;

    // 登录
    public Preference login(String username, String password) {
        Preference prefs = getPreference();
        Assert.notNull(prefs, "系统尚未初始化");
        Assert.isTrue(prefs.getUsername().equals(username)
            && bCryptEncoder.matches(password, prefs.getPassword()), "用户名或密码错误");
        return prefs;
    }

    // 获取偏好
    @Cacheable(value = "preference", key = "'#preference'")
    public Preference getPreference() {
        Optional<Preference> optional = prefsRepo.findById(Long.MAX_VALUE);
        return optional.orElse(null);
    }

    // 初始化偏好
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "preference", key = "'#preference'")
    public Preference init(Preference prefs) {
        checkLibrary(prefs);
        prefs.setPassword(bCryptEncoder.encode(prefs.getPassword()));
        libraryService.scan(prefs.getLibrary());
        return prefsRepo.save(prefs);
    }

    // 保存偏好
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "preference", key = "'#preference'")
    public Preference save(Preference prefs) {
        checkLibrary(prefs);

        // 修改密码
        Preference _prefs = getPreference();
        String _library = _prefs.getLibrary();
        if (StringUtils.hasText(prefs.getPassword())) {
            prefs.setPassword(bCryptEncoder.encode(prefs.getPassword()));
        } else {
            prefs.setPassword(_prefs.getPassword());
        }

        // 变更监听目录
        String library = prefs.getLibrary();
        if (!library.equals(_library)) {
            metadataService.purge(library);
            libraryService.scan(library);
        }
        return prefsRepo.save(prefs);
    }

    // 校验媒体库路径
    private void checkLibrary(Preference prefs) {
        String library = prefs.getLibrary();
        File entry = new File(library);
        Assert.isTrue(entry.exists(), "媒体库路径不存在");
        Assert.isTrue(entry.isDirectory(), "媒体库路径必须为目录");
        prefs.setLibrary(library.replaceAll("[/\\\\]+$", ""));
    }

}