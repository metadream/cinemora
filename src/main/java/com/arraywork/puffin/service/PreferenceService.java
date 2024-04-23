package com.arraywork.puffin.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.Preference;
import com.arraywork.puffin.repo.PreferenceRepo;
import com.arraywork.springhood.external.BCryptEncoder;
import com.arraywork.springhood.util.Assert;

import jakarta.annotation.Resource;

/**
 * 偏好服务
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
@Service
public class PreferenceService {

    private static final String ID = "EYDXJNRJA5ZB6EIXEDCFLF1C";

    @Resource
    private BCryptEncoder bCryptEncoder;
    @Resource
    private PreferenceRepo preferenceRepo;

    // 获取偏好
    public Preference getPreference() {
        Optional<Preference> optional = preferenceRepo.findById(ID);
        return optional.orElse(null);
    }

    // 初始化偏好
    @Transactional(rollbackFor = Exception.class)
    public Preference init(Preference preference) {
        preference.setId(ID);
        preference.setPassword(bCryptEncoder.encode(preference.getPassword()));
        return preference;
    }

    // 保存偏好
    @Transactional(rollbackFor = Exception.class)
    public Preference savePreference(Preference entity) {
        Preference preference = preferenceRepo.getReferenceById(ID);
        preference.setLibrary(entity.getLibrary());
        preference.setMetafields(entity.getMetafields());
        preference.setSyncRenameFile(entity.isSyncRenameFile());
        return preference;
    }

    // 更新密码
    @Transactional(rollbackFor = Exception.class)
    public Preference savePassword(Preference entity) {
        Preference preference = preferenceRepo.getReferenceById(ID);
        Assert.isTrue(bCryptEncoder.matches(entity.getOldPassword(), preference.getPassword()), "原密码错误");
        preference.setPassword(bCryptEncoder.encode(entity.getPassword()));
        return preference;
    }

}