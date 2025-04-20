package com.arraywork.cinemora.service;

import java.io.File;
import java.nio.file.Path;
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
import com.arraywork.cinemora.entity.Settings;
import com.arraywork.cinemora.repo.SettingRepo;

/**
 * 偏好设置服务
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class SettingService implements SecurityService {

    @Resource
    private SecuritySession session;
    @Resource
    @Lazy
    private LibraryService libraryService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private SettingRepo settingRepo;

    // 登录
    @Override
    public Settings login(String username, String password) {
        Settings settings = getSettings();
        Assert.notNull(settings, "系统尚未初始化");
        Assert.isTrue(settings.getUsername().equals(username)
            && BCryptCipher.matches(password, settings.getPassword()), "用户名或密码错误");
        return settings;
    }

    // 获取偏好
    @Cacheable(value = "cinemora", key = "'#settings'")
    public Settings getSettings() {
        return settingRepo.findById(Long.MAX_VALUE).orElse(null);
    }

    // 获取媒体库
    public Path getLibrary() {
        return Path.of(getSettings().getLibrary());
    }

    // 初始化偏好
    @CachePut(value = "cinemora", key = "'#settings'")
    @Transactional(rollbackFor = Exception.class)
    public Settings init(Settings settings) throws Exception {
        checkLibrary(settings);
        settings.setPassword(BCryptCipher.encode(settings.getPassword()));

        session.setPrincipal(settings);
        //        libraryService.scan(settings.getLibrary(), true); // TODO
        return settingRepo.save(settings);
    }

    // 保存偏好
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "cinemora", key = "'#settings'")
    public Settings save(Settings settings) throws Exception {
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
            metadataService.purge(library);
            //            libraryService.scan(library, true); // TODO
        }
        return settingRepo.save(settings);
    }

    // 校验媒体库路径
    private void checkLibrary(Settings settings) {
        String library = settings.getLibrary();
        File entry = new File(library);
        Assert.isTrue(entry.exists(), "The media library path does not exist.");
        Assert.isTrue(entry.isDirectory(), "The media library path must be a directory.");
        settings.setLibrary(Path.of(library).toString());
    }

}