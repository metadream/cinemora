package com.arraywork.puffin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraywork.puffin.entity.User;

/**
 * User Repository
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/02/21
 */
public interface UserRepo extends JpaRepository<User, Long> {}