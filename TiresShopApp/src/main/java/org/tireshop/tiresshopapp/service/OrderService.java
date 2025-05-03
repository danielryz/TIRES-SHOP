package org.tireshop.tiresshopapp.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateOrderRequest;
import org.tireshop.tiresshopapp.dto.request.create.OrderItemRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateOrderStatusRequest;
import org.tireshop.tiresshopapp.dto.response.OrderItemResponse;
import org.tireshop.tiresshopapp.dto.response.OrderResponse;
import org.tireshop.tiresshopapp.entity.*;
import org.tireshop.tiresshopapp.exception.CartIsEmptyException;
import org.tireshop.tiresshopapp.exception.NotEnoughStockException;
import org.tireshop.tiresshopapp.exception.OrderNotFoundException;
import org.tireshop.tiresshopapp.exception.ProductNotFoundException;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.OrderRepository;
import org.tireshop.tiresshopapp.repository.ProductRepository;

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
  private final HttpSession session;

  // POST
  @Transactional
  public OrderResponse createOrder(CreateOrderRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Order order = new Order();
    order.setStatus(OrderStatus.CREATED);
    order.setCreatedAt(LocalDateTime.now());

    // login user
    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
      User user = userService.getCurrentUser();
      order.setUser(user);

      List<CartItem> cartItems = cartItemRepository.findByUser(user);
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

      // Clear basket
      cartItemRepository.deleteByUser(user);
    } else {
      order.setGuestName(request.guestFirstName() + " " + request.guestLastName());
      order.setEmail(request.guestEmail());
      order.setPhoneNumber(request.guestPhoneNumber());
      order.setSessionId(session.getId());

      if (request.items().isEmpty()) {
        throw new CartIsEmptyException();
      }

      List<OrderItem> orderItems = request.items().stream().map(orderItemRequest -> {
        Product product = productRepository.findById(orderItemRequest.productId())
            .orElseThrow(() -> new ProductNotFoundException(orderItemRequest.productId()));

        if (product.getStock() < orderItemRequest.quantity()) {
          throw new NotEnoughStockException();
        }

        product.setStock(product.getStock() - orderItemRequest.quantity());
        productRepository.save(product);

        return mapGuestItemToOrderItem(orderItemRequest, order);
      }).collect(Collectors.toList());


      order.setItems(orderItems);
    }
    // totalAmountOrder
    BigDecimal totalAmount = order.getItems().stream()
        .map(orderItem -> orderItem.getPriceAtPurchase()
            .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // save order
    orderRepository.save(order);



    return mapOrderToResponse(order, totalAmount);
  }

  // GET
  public List<OrderResponse> getUserOrders() {
    User user = userService.getCurrentUser();
    return orderRepository.findByUser(user).stream().map(this::mapOrderToResponse)
        .collect(Collectors.toList());
  }

  public OrderResponse getUserOrderById(Long id) {
    User user = userService.getCurrentUser();
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new AccessDeniedException("Access denied");
    }
    return mapOrderToResponse(order);
  }

  // GET for Admin
  public List<OrderResponse> getAllOrdersByStatus(OrderStatus status) {
    return orderRepository.findByStatus(status).stream().map(this::mapOrderToResponse)
        .collect(Collectors.toList());
  }

  public OrderResponse getOrderByIdAdmin(Long id) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    return mapOrderToResponse(order);
  }

  // PATCH STATUS for ADMIN MANAGER
  public void updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    order.setStatus(request.status());
    orderRepository.save(order);
  }

  // PATCH Status CANCEL for user
  public void cancelOrder(Long id) {
    User user = userService.getCurrentUser();
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
      throw new AccessDeniedException("Access denied to cancel order.");
    }

    if (order.getStatus() != OrderStatus.CREATED) {
      throw new RuntimeException("You cannot cancel an order that is in progress.");
    }
    order.setStatus(OrderStatus.CANCELLED);
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

  private OrderItem mapGuestItemToOrderItem(OrderItemRequest orderItemRequest, Order order) {
    Product product = productRepository.findById(orderItemRequest.productId())
        .orElseThrow(() -> new ProductNotFoundException(orderItemRequest.productId()));

    OrderItem orderItem = new OrderItem();
    orderItem.setOrder(order);
    orderItem.setProduct(product);
    orderItem.setQuantity(orderItemRequest.quantity());
    orderItem.setPriceAtPurchase(product.getPrice());
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
        order.getGuestName() != null ? order.getGuestName().split(" ")[1] : null);
  }

  private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
    BigDecimal totalPrice =
        orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
    return new OrderItemResponse(orderItem.getId(), orderItem.getProduct().getName(),
        orderItem.getQuantity(), orderItem.getPriceAtPurchase(), totalPrice);
  }

}
