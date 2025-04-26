package org.tireshop.tiresshopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tireshop.tiresshopapp.dto.request.create.AddToCartRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateCartItemRequest;
import org.tireshop.tiresshopapp.dto.response.CartItemResponse;
import org.tireshop.tiresshopapp.dto.response.CartSummaryResponse;
import org.tireshop.tiresshopapp.entity.CartItem;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.NotEnoughStockException;
import org.tireshop.tiresshopapp.exception.ProductNotFoundException;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;
  private final UserService userService;

  public User getCurrentUser() {
    return userService.getCurrentUser();
  }

  // GET
  public List<CartItemResponse> getCartForCurrentUser() {
    User user = userService.getCurrentUser();
    return cartItemRepository.findByUser(user).stream().map(this::mapToResponse).toList();
  }

  public CartSummaryResponse getCartSummary() {
    List<CartItemResponse> cartItems = getCartForCurrentUser();
    BigDecimal total =
        cartItems.stream().map(i -> i.pricePerItem().multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new CartSummaryResponse(cartItems, total);
  }

  // POST
  @Transactional
  public void addCartItem(AddToCartRequest request) {
    User user = userService.getCurrentUser();
    Product product = productRepository.findById(request.productId())
        .orElseThrow(() -> new ProductNotFoundException(request.productId()));

    CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
        .orElse(new CartItem(null, user, product, 0));

    if (cartItem.getQuantity() + request.quantity() > product.getStock()) {
      throw new NotEnoughStockException();
    }

    cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
    cartItemRepository.save(cartItem);
  }

  // PATCH
  @Transactional
  public void updateCartItem(Long id, UpdateCartItemRequest request) {
    CartItem cartItem = cartItemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("The item in the cart does not exist."));
    if (request.quantity() > cartItem.getProduct().getStock()) {
      throw new NotEnoughStockException();
    }

    cartItem.setQuantity(request.quantity());
    cartItemRepository.save(cartItem);
  }

  // DELETE
  @Transactional
  public void deleteCartItem(Long id) {
    cartItemRepository.deleteById(id);
  }

  @Transactional
  public void clearCartForUser(User user) {
    cartItemRepository.deleteByUser(user);
  }

  private CartItemResponse mapToResponse(CartItem cartItem) {
    BigDecimal pricePerItem = cartItem.getProduct().getPrice();
    BigDecimal totalPrice = pricePerItem.multiply(new BigDecimal(cartItem.getQuantity()));
    return new CartItemResponse(cartItem.getId(), cartItem.getProduct().getId(),
        cartItem.getProduct().getName(), cartItem.getQuantity(), pricePerItem, totalPrice);
  }
}
