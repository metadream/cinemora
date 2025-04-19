package com.arraywork.cinemora.entity;

import java.util.Set;

import lombok.Data;

/**
 * 标签云
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/05/02
 */
@Data
public class TagCloud {

    private Set<String> producers;
    private Set<String> directors;
    private Set<String> starring;
    private Set<String> series;
    private Set<String> genres;
    private Set<String> tags;

}