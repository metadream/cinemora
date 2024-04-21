package com.arraywork.puffin.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraywork.puffin.entity.Movie;

/**
 * Movie Repository
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/02/21
 */
public interface MovieRepo extends JpaRepository<Movie, Long> {}