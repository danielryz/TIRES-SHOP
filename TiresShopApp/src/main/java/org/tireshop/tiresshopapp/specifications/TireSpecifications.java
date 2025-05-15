package org.tireshop.tiresshopapp.specifications;


import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Tire;

import java.math.BigDecimal;

public class TireSpecifications {
    public static Specification<Tire> hasSeason(String seasonName) {
        return (root, query, criteriaBuilder) ->
                seasonName == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("season")), seasonName.toLowerCase());
    }

    public static Specification<Tire> hasSize(String size) {
        return (root, query, criteriaBuilder) ->
                size == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("size")), size.toLowerCase());
    }

    public static Specification<Tire> hasNameContaining(String name) {
        return (root,query,criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Tire> hasMinPrice(BigDecimal minPrice) {
        return (root,query,criteriaBuilder) ->
                minPrice == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("minPrice"), minPrice);
    }

    public static Specification<Tire> hasMaxPrice(BigDecimal maxPrice) {
        return (root,query,criteriaBuilder) ->
                maxPrice == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("maxPrice"), maxPrice);
    }
}
