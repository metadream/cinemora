package com.arraywork.puffin.spec;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.arraywork.puffin.entity.Metadata;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 查询元数据
 * @author AiChen
 * @created 2024/04/22
 */
public class MetadataSpec implements Specification<Metadata> {

    private static final long serialVersionUID = 4624074578174492514L;

    @Override
    public Predicate toPredicate(Root<Metadata> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        predicates.add(
            cb.equal(cb.function("JSON_ARRAY", String.class, root.get("producers"), cb.literal("$.[]")), "bbb"));

        Predicate[] p = new Predicate[predicates.size()];
        return cb.and(predicates.toArray(p));
    }

}