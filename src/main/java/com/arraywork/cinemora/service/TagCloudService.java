package com.arraywork.cinemora.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import com.arraywork.autumn.helper.ExpiringCache;
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
public class TagCloudService {

    private static final String TAG_CLOUD_KEY = "TAG_CLOUD_KEY";
    @Resource
    private ExpiringCache<String, TagCloud> cache;
    @Resource
    private MetadataRepo metadataRepo;

    /** 获取标签云 */
    public TagCloud getTagCloud() {
        TagCloud tagCloud = cache.get(TAG_CLOUD_KEY);
        if (tagCloud != null) return tagCloud;

        tagCloud = new TagCloud();
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
        List<Metadata> results = metadataRepo.findAll();
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
        cache.put(TAG_CLOUD_KEY, tagCloud);
        return tagCloud;
    }

    /** 清空缓存 */
    public void clearCache() {
        cache.remove(TAG_CLOUD_KEY);
    }

}