package com.arraywork.puffin.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.repo.PreferenceRepo;
import com.arraywork.springfield.external.BCryptEncoder;

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
    private PreferenceRepo preferenceRepo;

    // 登录
    public boolean login(Preference user) {
        return false;
    }

    // 获取偏好
    public Preference getPreference() {
        Optional<Preference> optional = preferenceRepo.findById(Preference.ID);
        return optional.orElse(null);
    }

    // 保存偏好
    @Transactional(rollbackFor = Exception.class)
    public Preference save(Preference preference) {
        preference.setPassword(bCryptEncoder.encode(preference.getPassword()));

        // TODO 校验路径是否存在
        return preferenceRepo.save(preference);
    }

}