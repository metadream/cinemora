package com.arraywork.puffin.spec;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.arraywork.puffin.entity.Metadata;

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
            predicates.add(cb.like(root.get("producers"), cb.literal("%\"" + keyword + "\"%")));
        }

        Predicate[] p = new Predicate[predicates.size()];
        return cb.and(predicates.toArray(p));
    }

}