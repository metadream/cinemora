package com.arraywork.cinemora.service;

import java.io.File;
import java.nio.file.Path;
import jakarta.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.arraywork.autumn.crypto.BCryptCipher;
import com.arraywork.autumn.security.SecurityService;
import com.arraywork.autumn.security.SecuritySession;
import com.arraywork.autumn.util.Assert;
import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.repo.SettingRepo;

/**
 * 系统设置服务
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
@CacheConfig(cacheNames = "cinemora")
public class SettingService implements SecurityService {

    @Resource
    private SecuritySession session;
    @Resource
    private SettingRepo settingRepo;

    /** 复写登录逻辑 */
    @Override
    public Settings login(String username, String password) {
        Settings settings = getSettings();
        Assert.notNull(settings, "系统尚未初始化");
        Assert.isTrue(settings.getUsername().equals(username)
            && BCryptCipher.matches(password, settings.getPassword()), "用户名或密码错误");
        return settings;
    }

    /** 获取设置（缓存） */
    @Cacheable(key = "'#settings'")
    public Settings getSettings() {
        return settingRepo.findById(Long.MAX_VALUE).orElse(null);
    }

    /** 获取媒体库路径 */
    public Path getLibrary() {
        return Path.of(getSettings().getLibrary());
    }

    /** 初始化设置 */
    @CachePut(key = "'#settings'")
    @Transactional(rollbackFor = Exception.class)
    public Settings init(Settings settings) {
        checkLibrary(settings);
        settings.setPassword(BCryptCipher.encode(settings.getPassword()));
        session.setPrincipal(settings);
        return settingRepo.save(settings);
    }

    /** 保存设置 */
    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "'#settings'")
    public Settings save(Settings settings) {
        checkLibrary(settings);

        // 修改密码
        Settings _settings = getSettings();
        String _library = _settings.getLibrary();
        if (StringUtils.hasText(settings.getPassword())) {
            settings.setPassword(BCryptCipher.encode(settings.getPassword()));
        } else {
            settings.setPassword(_settings.getPassword());
        }

        // 变更监听目录
        String library = settings.getLibrary();
        if (!library.equals(_library)) {
            //            metadataService.clean(library); // TODO
            //            libraryService.scan(library, true); // TODO
        }
        return settingRepo.save(settings);
    }

    /** 校验媒体库路径 */
    private void checkLibrary(Settings settings) {
        Path library = Path.of(settings.getLibrary());
        File dir = library.toFile();
        Assert.isTrue(dir.exists(), "The media library path does not exist.");
        Assert.isTrue(dir.isDirectory(), "The media library path must be a directory.");
        settings.setLibrary(library.toString());
    }

}