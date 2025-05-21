package org.tireshop.tiresshopapp.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Rim;

import java.math.BigDecimal;
import java.util.List;

public class RimSpecifications {

  public static Specification<Rim> hasMaterial(List<String> material) {
    return (root, query, criteriaBuilder) -> {
      if (material == null || material.isEmpty()) {
        return null;
      }
      CriteriaBuilder.In<String> inClause =
          criteriaBuilder.in(criteriaBuilder.lower(root.get("material")));
      for (String mat : material) {
        inClause.value(mat.toLowerCase());
      }
      return inClause;
    };
  }

  public static Specification<Rim> hasSize(List<String> size) {
    return (root, query, criteriaBuilder) -> {
      if (size == null || size.isEmpty()) {
        return null;
      }
      CriteriaBuilder.In<String> inClause =
          criteriaBuilder.in(criteriaBuilder.lower(root.get("size")));
      for (String s : size) {
        inClause.value(s.toLowerCase());
      }
      return inClause;
    };
  }

  public static Specification<Rim> hasBoltPattern(List<String> boltPattern) {
    return (root, query, criteriaBuilder) -> {
      if (boltPattern == null || boltPattern.isEmpty()) {
        return null;
      }
      CriteriaBuilder.In<String> inClause =
          criteriaBuilder.in(criteriaBuilder.lower(root.get("boltPattern")));
      for (String bp : boltPattern) {
        inClause.value(bp.toLowerCase());
      }
      return inClause;
    };
  }

  public static Specification<Rim> hasNameContaining(String name) {
    return (root, query, criteriaBuilder) -> name == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
            "%" + name.toLowerCase() + "%");
  }

  public static Specification<Rim> hasMinPrice(BigDecimal minPrice) {
    return (root, query, criteriaBuilder) -> minPrice == null ? null
        : criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
  }

  public static Specification<Rim> hasMaxPrice(BigDecimal maxPrice) {
    return (root, query, criteriaBuilder) -> maxPrice == null ? null
        : criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
  }
}
