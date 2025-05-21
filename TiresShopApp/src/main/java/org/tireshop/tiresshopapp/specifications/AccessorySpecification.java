package org.tireshop.tiresshopapp.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Accessory;
import org.tireshop.tiresshopapp.entity.AccessoryType;

import java.math.BigDecimal;
import java.util.List;

public class AccessorySpecification {

  public static Specification<Accessory> hasAccessoryType(List<AccessoryType> accessoryType) {
    return (root, query, criteriaBuilder) -> {
      if (accessoryType == null || accessoryType.isEmpty()) {
        return null;
      }
      CriteriaBuilder.In<AccessoryType> inClause = criteriaBuilder.in(root.get("accessoryType"));
      for (AccessoryType type : accessoryType) {
        inClause.value(type);
      }
      return inClause;
    };
  }

  public static Specification<Accessory> hasNameContaining(String name) {
    return (root, query, criteriaBuilder) -> name == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
            "%" + name.toLowerCase() + "%");
  }

  public static Specification<Accessory> hasMinPrice(BigDecimal minPrice) {
    return (root, query, criteriaBuilder) -> minPrice == null ? null
        : criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
  }

  public static Specification<Accessory> hasMaxPrice(BigDecimal maxPrice) {
    return (root, query, criteriaBuilder) -> maxPrice == null ? null
        : criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
  }
}
