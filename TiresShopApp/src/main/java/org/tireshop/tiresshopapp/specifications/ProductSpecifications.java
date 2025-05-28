package org.tireshop.tiresshopapp.specifications;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {

  public static Specification<Product> hasNameContaining(String name) {
    return (root, query, criteriaBuilder) -> name == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }

  public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
    return (root, query, criteriaBuilder) -> minPrice == null ? null
        : criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
  }

  public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
    return (root, query, criteriaBuilder) -> maxPrice == null ? null
        : criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
  }

  public static Specification<Product> hasProductType(ProductType type) {
    return (root, query, criteriaBuilder) -> type == null ? null
        : criteriaBuilder.equal(root.get("type"), type);
  }

  public static Specification<Product> hasKeywordContaining(String keyword) {
    return (root, query, criteriaBuilder) -> {
      if (keyword == null || keyword.isEmpty()) {
        return null;
      }
      String kw = "%" + keyword.toLowerCase() + "%";

      List<Predicate> predicates = new ArrayList<>();

      // Wspólne pola
      predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), kw));
      predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), kw));

      // Dziedziczenie: treatowanie jako podklasy
      try {
        Root<Accessory> accessoryRoot = criteriaBuilder.treat(root, Accessory.class);
        predicates.add(criteriaBuilder
            .like(criteriaBuilder.lower(accessoryRoot.get("accessoryType").as(String.class)), kw));
      } catch (IllegalArgumentException ignored) {
      }

      try {
        Root<Tire> tireRoot = criteriaBuilder.treat(root, Tire.class);
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(tireRoot.get("season")), kw));
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(tireRoot.get("size")), kw));
      } catch (IllegalArgumentException ignored) {
      }

      try {
        Root<Rim> rimRoot = criteriaBuilder.treat(root, Rim.class);
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(rimRoot.get("material")), kw));
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(rimRoot.get("boltPattern")), kw));
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(rimRoot.get("size")), kw));
      } catch (IllegalArgumentException ignored) {
      }

      return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    };
  }
}
