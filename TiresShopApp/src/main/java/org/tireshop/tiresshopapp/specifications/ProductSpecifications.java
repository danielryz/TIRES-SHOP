package org.tireshop.tiresshopapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Product> hasNameContaining(String name) {
        return (root,query,criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%");
    }

    public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
        return (root,query,criteriaBuilder) ->
                minPrice == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("minPrice"), minPrice);
    }

    public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
        return (root,query,criteriaBuilder) ->
                maxPrice == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("maxPrice"), maxPrice);
    }

    public static Specification<Product> hasProductType(ProductType productType) {
        return (root,query,criteriaBuilder) ->
                productType == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("productType")), productType.toString().toLowerCase());
    }
}
