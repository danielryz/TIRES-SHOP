package org.tireshop.tiresshopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tireshop.tiresshopapp.dto.request.create.CreateOrderRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateOrderStatusRequest;
import org.tireshop.tiresshopapp.dto.response.OrderItemResponse;
import org.tireshop.tiresshopapp.dto.response.OrderResponse;
import org.tireshop.tiresshopapp.entity.*;
import org.tireshop.tiresshopapp.exception.*;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.OrderRepository;
import org.tireshop.tiresshopapp.repository.ProductRepository;
import org.tireshop.tiresshopapp.specifications.OrderSpecifications;
import org.tireshop.tiresshopapp.util.SortUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final CartItemRepository cartItemRepository;
  private final UserService userService;

  public User getCurrentUser() {
    return userService.getCurrentUser();
  }

  // POST
  @Transactional
  public OrderResponse createOrder(CreateOrderRequest request, String clientId) {
    Order order = new Order();
    order.setStatus(OrderStatus.CREATED);
    order.setCreatedAt(LocalDateTime.now());
    List<CartItem> cartItems;

    // login user
    User user = getCurrentUser();
    if (user != null) {
      order.setUser(user);
      order.setGuestName(user.getFirstName() + " " + user.getLastName());
      order.setEmail(user.getEmail());
      order.setPhoneNumber(user.getPhoneNumber());

      cartItems = cartItemRepository.findByUser(user);

    } else if (clientId != null && !clientId.isEmpty()) {
      order.setGuestName(request.guestFirstName() + " " + request.guestLastName());
      order.setEmail(request.guestEmail());
      order.setPhoneNumber(request.guestPhoneNumber());
      order.setSessionId(clientId);

      cartItems = cartItemRepository.findBySessionId(clientId);

    } else {
      throw new UserNotFoundException();
    }

    if (cartItems.isEmpty()) {
      throw new CartIsEmptyException();
    }

    List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
      Product product = cartItem.getProduct();
      int quantity = cartItem.getQuantity();

      if (product.getStock() < quantity) {
        throw new NotEnoughStockException();
      }

      product.setStock(product.getStock() - quantity);
      productRepository.save(product);

      return mapCartItemToOrderItem(cartItem, order);
    }).collect(Collectors.toList());

    order.setItems(orderItems);

    if (user != null) {
      cartItemRepository.deleteByUser(user);
    } else {
      cartItemRepository.deleteBySessionId(clientId);
    }

    BigDecimal totalAmount = order.getItems().stream()
        .map(orderItem -> orderItem.getPriceAtPurchase()
            .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    orderRepository.save(order);

    return mapOrderToResponse(order, totalAmount);
  }


  // GET
  @Transactional(readOnly = true)
  public List<OrderResponse> getUserOrders() {
    User user = getCurrentUser();
    return orderRepository.findByUser(user).stream().map(this::mapOrderToResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public OrderResponse getUserOrderById(Long id, String clientId) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    User user = getCurrentUser();
    if (user != null && user.getId().equals(order.getUser().getId())) {
      return mapOrderToResponse(order);
    } else if (clientId != null && clientId.equals(order.getSessionId())) {
      return mapOrderToResponse(order);
    } else {
      throw new AccessDeniedException("Access denied to get user order");
    }
  }

  // GET for Admin

  @Transactional(readOnly = true)
  public Page<OrderResponse> getOrders(Long userId, OrderStatus status, LocalDateTime createdAtFrom,
      LocalDateTime createdAtTo, Boolean isPaid, LocalDateTime paidAtFrom, LocalDateTime paidAtTo,
      int page, int sizePerPage, String sort) {
    Specification<Order> specification = Specification.where(OrderSpecifications.hasUserId(userId))
        .and(OrderSpecifications.hasStatus(status))
        .and(OrderSpecifications.createdAtGreaterThan(createdAtFrom))
        .and(OrderSpecifications.createdAtLessThan(createdAtTo))
        .and(OrderSpecifications.paidAtGreaterThan(paidAtFrom))
        .and(OrderSpecifications.paidAtLessThan(paidAtTo))
        .and(OrderSpecifications.hasIsPaid(isPaid));

    Sort sorting = SortUtils.parseSort(sort);
    Pageable pageable = PageRequest.of(page, sizePerPage, sorting);
    Page<Order> orders = orderRepository.findAll(specification, pageable);
    return orders.map(this::mapOrderToResponse);
  }


  @Transactional(readOnly = true)
  public OrderResponse getOrderByIdAdmin(Long id) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    return mapOrderToResponse(order);
  }

  // PATCH STATUS for ADMIN MANAGER
  @Transactional
  public void updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    if (request.status() != null)
      order.setStatus(request.status());
    orderRepository.save(order);
  }

  // PATCH Status CANCEL for user
  @Transactional
  public void cancelOrder(Long id) {
    User user = getCurrentUser();
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
      throw new AccessDeniedException("Access denied to cancel order.");
    }

    if (order.getStatus() != OrderStatus.CREATED) {
      throw new OrderInProgressException();
    }
    order.setStatus(OrderStatus.CANCELLED);
    orderRepository.save(order);
  }

  @Transactional
  public void payForYourOrder(Long id, String clientId) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    User user = getCurrentUser();
    if (order.isPaid()) {
      throw new OrderIsPaidException();
    }

    if (user != null && user.getId().equals(order.getUser().getId())) {
      order.setPaid(true);
      order.setPaidAt(LocalDateTime.now());
    } else if (clientId != null && clientId.equals(order.getSessionId())) {
      order.setPaid(true);
      order.setPaidAt(LocalDateTime.now());
    } else {
      throw new AccessDeniedException("Access denied to mark as paid");
    }

    orderRepository.save(order);
  }

  private OrderItem mapCartItemToOrderItem(CartItem cartItem, Order order) {
    OrderItem orderItem = new OrderItem();
    orderItem.setOrder(order);
    orderItem.setProduct(cartItem.getProduct());
    orderItem.setQuantity(cartItem.getQuantity());
    orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
    return orderItem;
  }

  private OrderResponse mapOrderToResponse(Order order) {
    BigDecimal totalAmount = order.getItems().stream()
        .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return mapOrderToResponse(order, totalAmount);
  }

  private OrderResponse mapOrderToResponse(Order order, BigDecimal totalAmount) {
    return new OrderResponse(order.getId(), order.getStatus(), totalAmount,
        order.getItems().stream().map(this::mapToOrderItemResponse).toList(), order.getCreatedAt(),
        order.getEmail(), order.getGuestName() != null ? order.getGuestName().split(" ")[0] : null,
        order.getGuestName() != null ? order.getGuestName().split(" ")[1] : null, order.isPaid(),
        order.getPaidAt());
  }

  private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
    BigDecimal totalPrice =
        orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
    return new OrderItemResponse(orderItem.getId(), orderItem.getProduct().getName(),
        orderItem.getQuantity(), orderItem.getPriceAtPurchase(), totalPrice);
  }

}
