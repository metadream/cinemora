package com.arraywork.puffin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arraywork.puffin.entity.User;
import com.arraywork.puffin.repo.UserRepo;

import jakarta.annotation.Resource;

/**
 * 用户服务
 * @author AiChen
 * @created 2024/04/22
 */
@Service
public class UserService {

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
        return userRepo.save(user);
    }

}