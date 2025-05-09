package org.tireshop.tiresshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tireshop.tiresshopapp.entity.Order;
import org.tireshop.tiresshopapp.entity.OrderStatus;
import org.tireshop.tiresshopapp.entity.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByUser(User user);

  Optional<Order> findTopByUserOrderByCreatedAtDesc(User user);

  Optional<Order> findTopBySessionIdOrderByCreatedAtDesc(String sessionId);

  List<Order> findByStatus(OrderStatus status);
}
