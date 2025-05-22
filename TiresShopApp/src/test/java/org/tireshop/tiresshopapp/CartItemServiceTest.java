package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.tireshop.tiresshopapp.dto.request.create.AddToCartRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateCartItemRequest;
import org.tireshop.tiresshopapp.dto.response.CartItemResponse;
import org.tireshop.tiresshopapp.dto.response.CartSummaryResponse;
import org.tireshop.tiresshopapp.entity.*;
import org.tireshop.tiresshopapp.exception.*;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.ProductRepository;
import org.tireshop.tiresshopapp.service.CartItemService;
import org.tireshop.tiresshopapp.service.UserService;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartItemServiceTest {

  @Mock
  private CartItemRepository cartItemRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private CartItemService cartItemService;

  private User user;
  private Product product;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(1L);
    product = new Product();
    product.setId(1L);
    product.setName("Product A");
    product.setPrice(BigDecimal.valueOf(100));
    product.setStock(10);
  }

  @Test
  void shouldGetCartForLoggedUser() {
    CartItem cartItem = new CartItem(1L, user, null, product, 2);
    when(userService.getCurrentUser()).thenReturn(user);
    when(cartItemRepository.findByUser(user)).thenReturn(List.of(cartItem));

    List<CartItemResponse> responses = cartItemService.getCartForCurrentUser(null);

    assertEquals(1, responses.size());
    assertEquals("Product A", responses.get(0).productName());
    assertEquals(BigDecimal.valueOf(100), responses.get(0).pricePerItem());
  }

  @Test
  void shouldGetCartForAnonymousUserBySessionId() {
    CartItem cartItem = new CartItem(1L, null, "abc123", product, 2);
    when(userService.getCurrentUser()).thenReturn(null);
    when(cartItemRepository.findBySessionId("abc123")).thenReturn(List.of(cartItem));

    List<CartItemResponse> responses = cartItemService.getCartForCurrentUser("abc123");

    assertEquals(1, responses.size());
    assertEquals(2, responses.get(0).quantity());
  }

  @Test
    void shouldThrowUserNotFoundException_WhenNoUserAndNoClientId() {
        when(userService.getCurrentUser()).thenReturn(null);

        assertThrows(UserNotFoundException.class,
                () -> cartItemService.getCartForCurrentUser(null));
    }

  @Test
  void shouldReturnCartSummaryWithTotal() {
    CartItem cartItem = new CartItem(1L, user, null, product, 2);
    when(userService.getCurrentUser()).thenReturn(user);
    when(cartItemRepository.findByUser(user)).thenReturn(List.of(cartItem));

    CartSummaryResponse response = cartItemService.getCartSummary(null);

    assertEquals(BigDecimal.valueOf(200), response.total());
    assertEquals(1, response.items().size());
  }

  @Test
  void shouldAddCartItemToExistingItem() {
    AddToCartRequest request = new AddToCartRequest(1L, 3);
    CartItem cartItem = new CartItem(1L, user, null, product, 2);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(userService.getCurrentUser()).thenReturn(user);
    when(cartItemRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(cartItem));

    cartItemService.addCartItem(request, null);

    verify(cartItemRepository, times(1)).save(cartItem);
    assertEquals(5, cartItem.getQuantity());
  }

  @Test
  void shouldAddNewCartItemIfNotExists() {
    AddToCartRequest request = new AddToCartRequest(1L, 1);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(userService.getCurrentUser()).thenReturn(user);
    when(cartItemRepository.findByUserAndProduct(user, product)).thenReturn(Optional.empty());

    cartItemService.addCartItem(request, null);

    verify(cartItemRepository).save(any(CartItem.class));
  }

  @Test
  void shouldThrowStockException_WhenAddingTooMuch() {
    AddToCartRequest request = new AddToCartRequest(1L, 11);
    CartItem cartItem = new CartItem(1L, user, null, product, 0);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(userService.getCurrentUser()).thenReturn(user);
    when(cartItemRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(cartItem));

    assertThrows(NotEnoughStockException.class, () -> cartItemService.addCartItem(request, null));
  }

  @Test
  void shouldUpdateCartItem() {
    CartItem cartItem = new CartItem(1L, user, null, product, 1);
    UpdateCartItemRequest request = new UpdateCartItemRequest(5);

    when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

    cartItemService.updateCartItem(1L, request);

    assertEquals(5, cartItem.getQuantity());
    verify(cartItemRepository).save(cartItem);
  }

  @Test
  void shouldThrowNotEnoughStock_WhenUpdating() {
    UpdateCartItemRequest request = new UpdateCartItemRequest(11);
    CartItem cartItem = new CartItem(1L, user, null, product, 1);

    when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

    assertThrows(NotEnoughStockException.class, () -> cartItemService.updateCartItem(1L, request));
  }

  @Test
    void shouldDeleteCartItem_WhenExists() {
        when(cartItemRepository.existsById(1L)).thenReturn(true);

        cartItemService.deleteCartItem(1L);

        verify(cartItemRepository).deleteById(1L);
    }

  @Test
    void shouldThrowException_WhenDeletingNonExistingItem() {
        when(cartItemRepository.existsById(1L)).thenReturn(false);

        assertThrows(ItemNonExistInCartException.class,
                () -> cartItemService.deleteCartItem(1L));
    }

  @Test
    void shouldClearCartForUser() {
        when(userService.getCurrentUser()).thenReturn(user);

        cartItemService.clearCartForUser(null);

        verify(cartItemRepository).deleteByUser(user);
    }

  @Test
    void shouldClearCartForAnonymousSession() {
        when(userService.getCurrentUser()).thenReturn(null);

        cartItemService.clearCartForUser("abc123");

        verify(cartItemRepository).deleteBySessionId("abc123");
    }

  @Test
    void shouldThrowException_WhenClearingCartAndNoUserOrSession() {
        when(userService.getCurrentUser()).thenReturn(null);

        assertThrows(UserNotFoundException.class,
                () -> cartItemService.clearCartForUser(null));
    }
}
