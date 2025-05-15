package org.tireshop.tiresshopapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Accessory;
import org.tireshop.tiresshopapp.entity.AccessoryType;

import java.math.BigDecimal;

public class AccessorySpecification {

    public static Specification<Accessory> hasAccessoryType(AccessoryType accessoryType) {
        return (root,query,criteriaBuilder) ->
                accessoryType == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("accessoryType")), accessoryType.toString().toLowerCase());
    }

    public static Specification<Accessory> hasNameContaining(String name) {
        return (root,query,criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Accessory> hasMinPrice(BigDecimal minPrice) {
        return (root,query,criteriaBuilder) ->
                minPrice == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("minPrice"), minPrice);
    }

    public static Specification<Accessory> hasMaxPrice(BigDecimal maxPrice) {
        return (root,query,criteriaBuilder) ->
                maxPrice == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("maxPrice"), maxPrice);
    }
}
