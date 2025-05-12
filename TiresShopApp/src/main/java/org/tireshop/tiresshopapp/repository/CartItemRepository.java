package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.entity.CartItem;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  List<CartItem> findByUser(User user);

  Optional<CartItem> findByUserAndProduct(User user, Product product);

  List<CartItem> findBySessionId(String sessionId);

  Optional<CartItem> findBySessionIdAndProduct(String sessionId, Product product);

  void deleteBySessionId(String sessionId);

  void deleteByUser(User user);
}
