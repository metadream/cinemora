package com.arraywork.cinemora.service;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import jakarta.annotation.Resource;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.arraywork.autumn.crypto.BCryptCipher;
import com.arraywork.autumn.security.SecurityService;
import com.arraywork.autumn.security.SecuritySession;
import com.arraywork.autumn.util.Assert;
import com.arraywork.cinemora.entity.Preference;
import com.arraywork.cinemora.repo.PreferenceRepo;

/**
 * 偏好设置服务
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class PreferenceService implements SecurityService {

    @Resource
    private SecuritySession session;
    @Resource
    @Lazy
    private LibraryService libraryService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private PreferenceRepo prefsRepo;

    // 登录
    @Override
    public Preference login(String username, String password) {
        Preference prefs = getPreference();
        Assert.notNull(prefs, "系统尚未初始化");
        Assert.isTrue(prefs.getUsername().equals(username)
            && BCryptCipher.matches(password, prefs.getPassword()), "用户名或密码错误");
        return prefs;
    }

    // 获取偏好
    @Cacheable(value = "cinemora", key = "'#preference'")
    public Preference getPreference() {
        Optional<Preference> optional = prefsRepo.findById(Long.MAX_VALUE);
        return optional.orElse(null);
    }

    // 初始化偏好
    @CachePut(value = "cinemora", key = "'#preference'")
    @Transactional(rollbackFor = Exception.class)
    public Preference init(Preference prefs) throws Exception {
        checkLibrary(prefs);
        prefs.setPassword(BCryptCipher.encode(prefs.getPassword()));

        session.setPrincipal(prefs);
        libraryService.scan(prefs.getLibrary(), true);
        return prefsRepo.save(prefs);
    }

    // 保存偏好
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "cinemora", key = "'#preference'")
    public Preference save(Preference prefs) throws Exception {
        checkLibrary(prefs);

        // 修改密码
        Preference _prefs = getPreference();
        String _library = _prefs.getLibrary();
        if (StringUtils.hasText(prefs.getPassword())) {
            prefs.setPassword(BCryptCipher.encode(prefs.getPassword()));
        } else {
            prefs.setPassword(_prefs.getPassword());
        }

        // 变更监听目录
        String library = prefs.getLibrary();
        if (!library.equals(_library)) {
            metadataService.purge(library);
            libraryService.scan(library, true);
        }
        return prefsRepo.save(prefs);
    }

    // 校验媒体库路径
    private void checkLibrary(Preference prefs) {
        String library = prefs.getLibrary();
        File entry = new File(library);
        Assert.isTrue(entry.exists(), "媒体库路径不存在");
        Assert.isTrue(entry.isDirectory(), "媒体库路径必须为目录");
        prefs.setLibrary(Path.of(library).toString());
    }

}