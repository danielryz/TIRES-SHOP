package org.tireshop.tiresshopapp.specifications;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Tire;

import java.math.BigDecimal;
import java.util.List;

public class TireSpecifications {
  public static Specification<Tire> hasSeasons(List<String> seasonNames) {
    return (root, query, criteriaBuilder) -> {
      if (seasonNames == null || seasonNames.isEmpty()) {
        return null;
      }

      CriteriaBuilder.In<String> inClause =
          criteriaBuilder.in(criteriaBuilder.lower(root.get("season")));
      for (String season : seasonNames) {
        inClause.value(season.toLowerCase());
      }
      return inClause;
    };
  }

  public static Specification<Tire> hasSizes(List<String> sizes) {
    return (root, query, criteriaBuilder) -> {
      if (sizes == null || sizes.isEmpty()) {
        return null;
      }

      CriteriaBuilder.In<String> inClause =
          criteriaBuilder.in(criteriaBuilder.lower(root.get("size")));
      for (String size : sizes) {
        inClause.value(size.toLowerCase());
      }
      return inClause;
    };
  }

  public static Specification<Tire> hasNameContaining(String name) {
    return (root, query, criteriaBuilder) -> name == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
            "%" + name.toLowerCase() + "%");
  }

  public static Specification<Tire> hasMinPrice(BigDecimal minPrice) {
    return (root, query, criteriaBuilder) -> minPrice == null ? null
        : criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
  }

  public static Specification<Tire> hasMaxPrice(BigDecimal maxPrice) {
    return (root, query, criteriaBuilder) -> maxPrice == null ? null
        : criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
  }
}
