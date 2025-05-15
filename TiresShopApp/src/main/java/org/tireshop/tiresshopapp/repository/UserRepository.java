package org.tireshop.tiresshopapp.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM user_roles WHERE user_id = :userId AND role_id = :roleId",
      nativeQuery = true)
  void deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
