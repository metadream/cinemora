package com.arraywork.puffin.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraywork.puffin.entity.User;

/**
 * 用户持久化
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/02/21
 */
public interface UserRepo extends JpaRepository<User, String> {

    User findByIsSuperTrue();
    User findByUsername(String username);

}