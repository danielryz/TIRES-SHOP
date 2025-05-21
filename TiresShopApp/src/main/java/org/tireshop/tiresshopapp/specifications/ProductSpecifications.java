package org.tireshop.tiresshopapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public class ProductSpecifications {

  public static Specification<Product> hasNameContaining(String name) {
    return (root, query, criteriaBuilder) -> name == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%");
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
}
