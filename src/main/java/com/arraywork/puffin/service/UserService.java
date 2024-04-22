package com.arraywork.puffin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.User;
import com.arraywork.puffin.repo.UserRepo;
import com.arraywork.springhood.external.BCryptEncoder;
import com.arraywork.springhood.util.Assert;

import jakarta.annotation.Resource;

/**
 * 用户服务
 * @author AiChen
 * @created 2024/04/22
 */
@Service
public class UserService {

    @Resource
    private BCryptEncoder bCryptEncoder;
    @Resource
    private UserRepo userRepo;

    // 是否存在超级用户
    public boolean hasSuperUser() {
        User user = userRepo.findByIsSuperTrue();
        return user != null;
    }

    // 保存用户
    @Transactional(rollbackFor = Exception.class)
    public User saveUser(User user) {
        User _user = userRepo.findByUsername(user.getUsername());
        Assert.isNull(_user, "用户名已经存在");
        user.setPassword(bCryptEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

}