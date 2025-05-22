package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.tireshop.tiresshopapp.dto.request.create.CreateOrderRequest;
import org.tireshop.tiresshopapp.dto.response.OrderResponse;
import org.tireshop.tiresshopapp.entity.*;
import org.tireshop.tiresshopapp.exception.*;
import org.tireshop.tiresshopapp.repository.*;
import org.tireshop.tiresshopapp.service.OrderService;
import org.tireshop.tiresshopapp.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private CartItemRepository cartItemRepository;
  @Mock
  private UserService userService;
  @Mock
  private ShippingAddressRepository shippingAddressRepository;

  @InjectMocks
  private OrderService orderService;

  private User mockUser;
  private Product mockProduct;
  private CartItem mockCartItem;
  private Order mockOrder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    mockUser = new User();
    mockUser.setId(1L);
    mockUser.setFirstName("John");
    mockUser.setLastName("Doe");
    mockUser.setEmail("john@example.com");
    mockUser.setPhoneNumber("123456789");

    mockProduct = new Product();
    mockProduct.setId(10L);
    mockProduct.setName("Test Product");
    mockProduct.setPrice(BigDecimal.valueOf(100));
    mockProduct.setStock(10);

    mockCartItem = new CartItem();
    mockCartItem.setId(100L);
    mockCartItem.setProduct(mockProduct);
    mockCartItem.setQuantity(2);
    mockCartItem.setUser(mockUser);

    mockOrder = new Order();
    mockOrder.setId(1000L);
    mockOrder.setUser(mockUser);
    mockOrder.setStatus(OrderStatus.CREATED);
    mockOrder.setItems(new ArrayList<>());
    mockOrder.setShippingAddress(new ShippingAddress());
    mockOrder.setCreatedAt(LocalDateTime.now());
  }

  @Test
  void testCreateOrder_WithLoggedUser_Success() {
    CreateOrderRequest request =
        new CreateOrderRequest("Street", "12A", "3", "00-123", "City", null, null, null, null);

    when(userService.getCurrentUser()).thenReturn(mockUser);
    when(cartItemRepository.findByUser(mockUser)).thenReturn(List.of(mockCartItem));
    when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
    when(shippingAddressRepository.save(any(ShippingAddress.class)))
        .thenAnswer(i -> i.getArgument(0));
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
      Order o = i.getArgument(0);
      o.setId(1000L);
      return o;
    });

    OrderResponse response = orderService.createOrder(request, null);

    assertNotNull(response);
    assertEquals(OrderStatus.CREATED, response.status());
    assertEquals(BigDecimal.valueOf(200), response.totalAmount());
    verify(cartItemRepository).deleteByUser(mockUser);
  }

  @Test
  void testCreateOrder_WithGuestUser_Success() {
    CreateOrderRequest request = new CreateOrderRequest("Street", "12A", "3", "00-123", "City",
        "Guest", "User", "guest@example.com", "987654321");

    when(userService.getCurrentUser()).thenReturn(null);
    when(cartItemRepository.findBySessionId("client123")).thenReturn(List.of(mockCartItem));
    when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
    when(shippingAddressRepository.save(any(ShippingAddress.class)))
        .thenAnswer(i -> i.getArgument(0));
    when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
      Order o = i.getArgument(0);
      o.setId(1001L);
      return o;
    });

    OrderResponse response = orderService.createOrder(request, "client123");

    assertNotNull(response);
    assertEquals(OrderStatus.CREATED, response.status());
    assertEquals(BigDecimal.valueOf(200), response.totalAmount());
    verify(cartItemRepository).deleteBySessionId("client123");
  }

  @Test
  void testCreateOrder_ThrowsUserNotFoundException() {
    CreateOrderRequest request =
        new CreateOrderRequest("Street", "12A", "3", "00-123", "City", null, null, null, null);

    when(userService.getCurrentUser()).thenReturn(null);

    Exception exception =
        assertThrows(UserNotFoundException.class, () -> orderService.createOrder(request, null));

    assertEquals(UserNotFoundException.class, exception.getClass());
  }

  @Test
  void testCreateOrder_ThrowsCartIsEmptyException() {
    CreateOrderRequest request =
        new CreateOrderRequest("Street", "12A", "3", "00-123", "City", null, null, null, null);

    when(userService.getCurrentUser()).thenReturn(mockUser);
    when(cartItemRepository.findByUser(mockUser)).thenReturn(Collections.emptyList());

    assertThrows(CartIsEmptyException.class, () -> orderService.createOrder(request, null));
  }

  @Test
  void testCreateOrder_ThrowsNotEnoughStockException() {
    CreateOrderRequest request =
        new CreateOrderRequest("Street", "12A", "3", "00-123", "City", null, null, null, null);

    mockProduct.setStock(1);
    mockCartItem.setQuantity(2);

    when(userService.getCurrentUser()).thenReturn(mockUser);
    when(cartItemRepository.findByUser(mockUser)).thenReturn(List.of(mockCartItem));

    assertThrows(NotEnoughStockException.class, () -> orderService.createOrder(request, null));
  }

  @Test
    void testGetUserOrders() {
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(orderRepository.findByUser(mockUser)).thenReturn(List.of(mockOrder));

        List<OrderResponse> orders = orderService.getUserOrders();

        assertEquals(1, orders.size());
        assertEquals(mockOrder.getId(), orders.get(0).id());
    }

  @Test
    void testGetUserOrderById_Success() {
        when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));
        when(userService.getCurrentUser()).thenReturn(mockUser);

        OrderResponse response = orderService.getUserOrderById(1000L, null);

        assertNotNull(response);
        assertEquals(mockOrder.getId(), response.id());
    }

  @Test
  void testGetUserOrderById_AccessDenied() {
    User otherUser = new User();
    otherUser.setId(999L);
    mockOrder.setUser(otherUser);

    when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));
    when(userService.getCurrentUser()).thenReturn(mockUser);

    assertThrows(AccessDeniedException.class, () -> orderService.getUserOrderById(1000L, null));
  }

  @Test
  void testGetOrders_Admin() {
    Page<Order> page = new PageImpl<>(List.of(mockOrder));
    when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

    Page<OrderResponse> result =
        orderService.getOrders(null, null, null, null, null, null, null, 0, 10, "");

    assertEquals(1, result.getTotalElements());
  }

  @Test
    void testGetOrderByIdAdmin_Success() {
        when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));

        OrderResponse response = orderService.getOrderByIdAdmin(1000L);

        assertNotNull(response);
        assertEquals(mockOrder.getId(), response.id());
    }

  @Test
    void testUpdateOrderStatus_Success() {
        when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));

        orderService.updateOrderStatus(1000L, OrderStatus.IN_PROGRESS);

        assertEquals(OrderStatus.IN_PROGRESS, mockOrder.getStatus());
        verify(orderRepository).save(mockOrder);
    }

  @Test
    void testCancelOrder_Success() {
        when(userService.getCurrentUser()).thenReturn(mockUser);
        mockOrder.setUser(mockUser);
        mockOrder.setStatus(OrderStatus.CREATED);
        when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));

        orderService.cancelOrder(1000L);

        assertEquals(OrderStatus.CANCELLED, mockOrder.getStatus());
        verify(orderRepository).save(mockOrder);
    }

  @Test
    void testCancelOrder_AccessDenied() {
        when(userService.getCurrentUser()).thenReturn(mockUser);
        User otherUser = new User();
        otherUser.setId(999L);
        mockOrder.setUser(otherUser);
        when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));

        assertThrows(AccessDeniedException.class, () -> orderService.cancelOrder(1000L));
    }

  @Test
    void testCancelOrder_OrderInProgress() {
        when(userService.getCurrentUser()).thenReturn(mockUser);
        mockOrder.setUser(mockUser);
        mockOrder.setStatus(OrderStatus.IN_PROGRESS);
        when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));

        assertThrows(Exception.class, () -> orderService.cancelOrder(1000L));
    }

  @Test
    void testPayForYourOrder_Success_User() {
        when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));
        when(userService.getCurrentUser()).thenReturn(mockUser);
        mockOrder.setUser(mockUser);
        mockOrder.setPaid(false);

        orderService.payForYourOrder(1000L, null);

        assertTrue(mockOrder.isPaid());
        assertNotNull(mockOrder.getPaidAt());
        verify(orderRepository).save(mockOrder);
    }

  @Test
    void testPayForYourOrder_Success_Guest() {
        when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));
        when(userService.getCurrentUser()).thenReturn(null);
        mockOrder.setSessionId("client123");
        mockOrder.setPaid(false);

        orderService.payForYourOrder(1000L, "client123");

        assertTrue(mockOrder.isPaid());
        assertNotNull(mockOrder.getPaidAt());
        verify(orderRepository).save(mockOrder);
    }

  @Test
  void testPayForYourOrder_AlreadyPaid() {
    mockOrder.setPaid(true);
    when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));

    assertThrows(Exception.class, () -> orderService.payForYourOrder(1000L, null));
  }

  @Test
  void testPayForYourOrder_AccessDenied() {
    mockOrder.setPaid(false);
    mockOrder.setUser(new User());
    mockOrder.getUser().setId(999L);

    when(orderRepository.findById(1000L)).thenReturn(Optional.of(mockOrder));
    when(userService.getCurrentUser()).thenReturn(mockUser);

    assertThrows(AccessDeniedException.class,
        () -> orderService.payForYourOrder(1000L, "client123"));
  }
}
