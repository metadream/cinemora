package com.arraywork.puffin.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.arraywork.puffin.entity.Metadata;
import com.arraywork.puffin.enums.Censorship;
import com.arraywork.puffin.enums.Quality;
import com.arraywork.puffin.enums.Rating;
import com.arraywork.puffin.enums.Region;
import com.arraywork.springforce.util.Arrays;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 查询元数据
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/04/22
 */
public class MetadataSpec implements Specification<Metadata> {

    private static final long serialVersionUID = 4624074578174492514L;
    private Metadata condition;

    public MetadataSpec(Metadata condition) {
        this.condition = condition;
    }

    @Override
    public Predicate toPredicate(Root<Metadata> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        String keyword = condition.getKeyword();
        if (StringUtils.hasText(keyword)) {
            predicates.add(cb.or(
                cb.like(root.get("code"), "%" + keyword + "%"),
                cb.like(root.get("title"), "%" + keyword + "%"),
                cb.and(
                    root.get("producers").isNotNull(),
                    cb.like(root.get("producers"), cb.literal("%\"" + keyword + "\"%"))),
                cb.and(
                    root.get("directors").isNotNull(),
                    cb.like(root.get("directors"), cb.literal("%\"" + keyword + "\"%"))),
                cb.and(
                    root.get("starring").isNotNull(),
                    cb.like(root.get("starring"), cb.literal("%\"" + keyword + "\"%"))),
                cb.and(
                    root.get("series").isNotNull(),
                    cb.like(root.get("series"), cb.literal("%\"" + keyword + "\"%"))),
                cb.and(
                    root.get("genres").isNotNull(),
                    cb.like(root.get("genres"), cb.literal("%\"" + keyword + "\"%"))),
                cb.and(
                    root.get("tags").isNotNull(),
                    cb.like(root.get("tags"), cb.literal("%\"" + keyword + "\"%")))
            // THE END
            ));
        }

        String[] producers = condition.getProducers();
        if (!Arrays.isEmpty(producers)) {
            predicates.add(cb.and(root.get("producers").isNotNull(),
                cb.like(root.get("producers"), cb.literal("%\"" + producers[0] + "\"%"))));
        }

        String[] directors = condition.getDirectors();
        if (!Arrays.isEmpty(directors)) {
            predicates.add(cb.and(root.get("directors").isNotNull(),
                cb.like(root.get("directors"), cb.literal("%\"" + directors[0] + "\"%"))));
        }

        String[] starring = condition.getStarring();
        if (!Arrays.isEmpty(starring)) {
            predicates.add(cb.and(root.get("starring").isNotNull(),
                cb.like(root.get("starring"), cb.literal("%\"" + starring[0] + "\"%"))));
        }

        String[] series = condition.getSeries();
        if (!Arrays.isEmpty(series)) {
            predicates.add(cb.and(root.get("series").isNotNull(),
                cb.like(root.get("series"), cb.literal("%\"" + series[0] + "\"%"))));
        }

        String[] genres = condition.getGenres();
        if (!Arrays.isEmpty(genres)) {
            predicates.add(cb.and(root.get("genres").isNotNull(),
                cb.like(root.get("genres"), cb.literal("%\"" + genres[0] + "\"%"))));
        }

        String[] tags = condition.getTags();
        if (!Arrays.isEmpty(tags)) {
            predicates.add(cb.and(root.get("tags").isNotNull(),
                cb.like(root.get("tags"), cb.literal("%\"" + tags[0] + "\"%"))));
        }

        Region region = condition.getRegion();
        if (region != null) {
            predicates.add(cb.equal(root.get("region"), region));
        }

        Quality quality = condition.getQuality();
        if (quality != null) {
            predicates.add(cb.equal(root.get("quality"), quality));
        }

        Censorship censorship = condition.getCensorship();
        if (censorship != null) {
            predicates.add(cb.equal(root.get("censorship"), censorship));
        }

        Rating rating = condition.getRating();
        if (rating != null) {
            predicates.add(cb.equal(root.get("rating"), rating));
        }

        boolean starred = condition.isStarred();
        if (starred) {
            predicates.add(cb.isTrue(root.get("starred")));
        }

        Predicate[] p = new Predicate[predicates.size()];
        return cb.and(predicates.toArray(p));
    }

}