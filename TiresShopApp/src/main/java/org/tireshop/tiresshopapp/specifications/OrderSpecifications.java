package org.tireshop.tiresshopapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.Order;
import org.tireshop.tiresshopapp.entity.OrderStatus;

import java.time.LocalDateTime;

public class OrderSpecifications {

  public static Specification<Order> hasUserId(Long userId) {
    return (root, query, criteriaBuilder) -> userId == null ? null
        : criteriaBuilder.equal(root.get("user").get("id"), userId);
  }

  public static Specification<Order> hasStatus(OrderStatus status) {
    return (root, query, criteriaBuilder) -> status == null ? null
        : criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")),
            status.toString().toLowerCase());
  }

  public static Specification<Order> createdAtGreaterThan(LocalDateTime createdAtGreaterThan) {
    return (root, query, criteriaBuilder) -> createdAtGreaterThan == null ? null
        : criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAtGreaterThan);
  }

  public static Specification<Order> createdAtLessThan(LocalDateTime createdAtLessThan) {
    return (root, query, criteriaBuilder) -> createdAtLessThan == null ? null
        : criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdAtLessThan);
  }

  public static Specification<Order> hasIsPaid(Boolean isPaid) {
    return (root, query, criteriaBuilder) -> isPaid == null ? null
        : criteriaBuilder.equal(root.get("isPaid"), isPaid);
  }

  public static Specification<Order> paidAtGreaterThan(LocalDateTime paidAtGreaterThan) {
    return (root, query, criteriaBuilder) -> paidAtGreaterThan == null ? null
        : criteriaBuilder.greaterThanOrEqualTo(root.get("paidAt"), paidAtGreaterThan);
  }

  public static Specification<Order> paidAtLessThan(LocalDateTime paidAtLessThan) {
    return (root, query, criteriaBuilder) -> paidAtLessThan == null ? null
        : criteriaBuilder.lessThan(root.get("paidAt"), paidAtLessThan);
  }
}
