package com.arraywork.cinemora.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.arraywork.cinemora.entity.Metadata;
import com.arraywork.cinemora.entity.TagCloud;
import com.arraywork.cinemora.repo.MetadataRepo;

/**
 * 标签云服务
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2024/05/02
 */
@Service
@CacheConfig(cacheNames = "cinemora")
public class TagCloudService {

    @Resource
    private MetadataRepo metadataRepo;

    /** 获取标签云 */
    @Cacheable(key = "'#tagcloud'")
    public TagCloud getTagCloud() {
        List<Metadata> results = metadataRepo.findTags();
        if (results.isEmpty()) return null;

        TagCloud tagCloud = new TagCloud();
        Set<String> allProducers = new HashSet<>();
        Set<String> allDirectors = new HashSet<>();
        Set<String> allStarring = new HashSet<>();
        Set<String> allSeries = new HashSet<>();
        Set<String> allGenres = new HashSet<>();
        Set<String> allTags = new HashSet<>();
        tagCloud.setProducers(allProducers);
        tagCloud.setDirectors(allDirectors);
        tagCloud.setStarring(allStarring);
        tagCloud.setSeries(allSeries);
        tagCloud.setGenres(allGenres);
        tagCloud.setTags(allTags);

        // 通过Set集合过滤重复值
        for (Metadata metadata : results) {
            String[] producers = metadata.getProducers();
            String[] directors = metadata.getDirectors();
            String[] starring = metadata.getStarring();
            String[] series = metadata.getSeries();
            String[] genres = metadata.getGenres();
            String[] tags = metadata.getTags();

            if (producers != null) allProducers.addAll(Arrays.asList(producers));
            if (directors != null) allDirectors.addAll(Arrays.asList(directors));
            if (starring != null) allStarring.addAll(Arrays.asList(starring));
            if (series != null) allSeries.addAll(Arrays.asList(series));
            if (genres != null) allGenres.addAll(Arrays.asList(genres));
            if (tags != null) allTags.addAll(Arrays.asList(tags));
        }
        return tagCloud;
    }

    /** 清空缓存 */
    @CacheEvict(key = "'#tagcloud'")
    public void clearCache() { }

}