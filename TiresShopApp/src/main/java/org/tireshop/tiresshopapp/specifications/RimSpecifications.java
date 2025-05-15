package org.tireshop.tiresshopapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Rim;

import java.math.BigDecimal;

public class RimSpecifications {

    public static Specification<Rim> hasMaterial(String material) {
        return (root, query, criteriaBuilder) ->
                material == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("material")), material.toLowerCase());
    }

    public static Specification<Rim> hasSize(String size) {
        return (root, query, criteriaBuilder) ->
                size == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("size")), size);
    }

    public static Specification<Rim> hasBoltPattern(String boltPattern) {
        return (root, query, criteriaBuilder) ->
                boltPattern == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("boltPattern")), boltPattern.toLowerCase());
    }

    public static Specification<Rim> hasNameContaining(String name) {
        return (root,query,criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Rim> hasMinPrice(BigDecimal minPrice) {
        return (root,query,criteriaBuilder) ->
                minPrice == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("minPrice"), minPrice);
    }

    public static Specification<Rim> hasMaxPrice(BigDecimal maxPrice) {
        return (root,query,criteriaBuilder) ->
                maxPrice == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("maxPrice"), maxPrice);
    }
}
