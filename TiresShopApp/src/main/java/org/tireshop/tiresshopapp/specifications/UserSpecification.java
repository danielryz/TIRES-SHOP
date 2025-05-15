package org.tireshop.tiresshopapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.entity.User;

public class UserSpecification {

  public static Specification<User> hasEmailContaining(String email) {
    return (root, query, criteriaBuilder) -> email == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
            "%" + email.toLowerCase() + "%");
  }

  public static Specification<User> hasUsernameContaining(String username) {
    return (root, query, criteriaBuilder) -> username == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
            "%" + username.toLowerCase() + "%");
  }

  public static Specification<User> hasFirstNameContaining(String firstName) {
    return (root, query, criteriaBuilder) -> firstName == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
            "%" + firstName.toLowerCase() + "%");
  }

  public static Specification<User> hasLastNameContaining(String lastName) {
    return (root, query, criteriaBuilder) -> lastName == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
            "%" + lastName.toLowerCase() + "%");
  }

  public static Specification<User> hasPhoneNumberContaining(String phoneNumber) {
    return (root, query, criteriaBuilder) -> phoneNumber == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")),
            "%" + phoneNumber.toLowerCase() + "%");
  }

  public static Specification<User> hasRole(String role) {
    return (root, query, criteriaBuilder) -> role == null ? null
        : criteriaBuilder.like(criteriaBuilder.lower(root.get("role")),
            "%" + role.toLowerCase() + "%");
  }
}
