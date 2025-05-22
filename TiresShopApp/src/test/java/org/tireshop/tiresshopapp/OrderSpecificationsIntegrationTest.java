package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.tireshop.tiresshopapp.entity.Order;
import org.tireshop.tiresshopapp.entity.OrderStatus;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.OrderItemRepository;
import org.tireshop.tiresshopapp.repository.OrderRepository;
import org.tireshop.tiresshopapp.repository.UserRepository;
import org.tireshop.tiresshopapp.specifications.OrderSpecifications;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderSpecificationsIntegrationTest {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;
  private User user1;
  private User user2;

  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    userRepository.deleteAll();
    cartItemRepository.deleteAll();
    orderItemRepository.deleteAll();

    user1 = new User();
    user1.setEmail("integration@test.pl");
    user1.setPhoneNumber("123456789");
    user1.setPassword("Pa$$word1");
    user1.setUsername("integration");
    user1.setFirstName("John");
    user1.setLastName("Doe");
    user1 = userRepository.save(user1);

    user2 = new User();
    user2.setEmail("integration@test2.pl");
    user2.setPhoneNumber("987654321");
    user2.setPassword("Pa$$word2");
    user2.setUsername("integration2");
    user2.setFirstName("Jane");
    user2.setLastName("Smith");
    user2 = userRepository.save(user2);

    orderRepository
        .save(new Order(user1, OrderStatus.CREATED, false, LocalDateTime.now().minusDays(5), null));
    orderRepository.save(new Order(user1, OrderStatus.CREATED, true,
        LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)));
    orderRepository.save(
        new Order(user2, OrderStatus.CANCELLED, false, LocalDateTime.now().minusDays(1), null));
  }

  @Test
  void testHasUserId() {
    Specification<Order> spec = OrderSpecifications.hasUserId(user1.getId());
    List<Order> result = orderRepository.findAll(spec);
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(order -> order.getUser().getId().equals(user1.getId())));

    List<Order> allOrders = orderRepository.findAll(OrderSpecifications.hasUserId(null));
    assertEquals(3, allOrders.size());
  }

  @Test
  void testHasStatus() {
    Specification<Order> spec = OrderSpecifications.hasStatus(OrderStatus.CREATED);
    List<Order> result = orderRepository.findAll(spec);
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(order -> order.getStatus() == OrderStatus.CREATED));

    result = orderRepository.findAll(OrderSpecifications.hasStatus(null));
    assertEquals(3, result.size());
  }

  @Test
  void testCreatedAtGreaterThan() {
    LocalDateTime since = LocalDateTime.now().minusDays(3);
    List<Order> result = orderRepository.findAll(OrderSpecifications.createdAtGreaterThan(since));
    assertEquals(2, result.size()); // order2, order3

    result = orderRepository.findAll(OrderSpecifications.createdAtGreaterThan(null));
    assertEquals(3, result.size());
  }

  @Test
  void testCreatedAtLessThan() {
    LocalDateTime until = LocalDateTime.now().minusDays(3);
    List<Order> result = orderRepository.findAll(OrderSpecifications.createdAtLessThan(until));
    assertEquals(1, result.size()); // only order1

    result = orderRepository.findAll(OrderSpecifications.createdAtLessThan(null));
    assertEquals(3, result.size());
  }

  @Test
  void testHasIsPaid() {
    List<Order> paidOrders = orderRepository.findAll(OrderSpecifications.hasIsPaid(true));
    assertEquals(1, paidOrders.size());
    assertTrue(paidOrders.get(0).isPaid());

    List<Order> unpaidOrders = orderRepository.findAll(OrderSpecifications.hasIsPaid(false));
    assertEquals(2, unpaidOrders.size());
    assertTrue(unpaidOrders.stream().noneMatch(Order::isPaid));

    List<Order> allOrders = orderRepository.findAll(OrderSpecifications.hasIsPaid(null));
    assertEquals(3, allOrders.size());
  }

  @Test
  void testPaidAtGreaterThan() {
    LocalDateTime date = LocalDateTime.now().minusDays(2);
    List<Order> result = orderRepository.findAll(OrderSpecifications.paidAtGreaterThan(date));
    assertEquals(1, result.size()); // only order2

    result = orderRepository.findAll(OrderSpecifications.paidAtGreaterThan(null));
    assertEquals(3, result.size());
  }

  @Test
  void testPaidAtLessThan() {
    LocalDateTime before = LocalDateTime.now().minusDays(2);
    List<Order> result = orderRepository.findAll(OrderSpecifications.paidAtLessThan(before));
    assertEquals(0, result.size());

    result = orderRepository
        .findAll(OrderSpecifications.paidAtLessThan(LocalDateTime.now().plusDays(1)));
    assertEquals(1, result.size()); // order2

    result = orderRepository.findAll(OrderSpecifications.paidAtLessThan(null));
    assertEquals(3, result.size());
  }
}
