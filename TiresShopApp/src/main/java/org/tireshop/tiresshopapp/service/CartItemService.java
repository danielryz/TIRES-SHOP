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
import org.tireshop.tiresshopapp.exception.*;
import org.tireshop.tiresshopapp.repository.CartItemRepository;
import org.tireshop.tiresshopapp.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
  public List<CartItemResponse> getCartForCurrentUser(String clientId) {
    User user = getCurrentUser();

    List<CartItem> items;
    if (user != null) {
      items = cartItemRepository.findByUser(user);
      return items.stream().map(this::mapToResponse).collect(Collectors.toList());
    } else if (clientId != null) {
      items = cartItemRepository.findBySessionId(clientId);
      return items.stream().map(this::mapToResponse).collect(Collectors.toList());
    } else {
      throw new UserNotFoundException();
    }
  }

  public CartSummaryResponse getCartSummary(String clientId) {
    List<CartItemResponse> cartItems = getCartForCurrentUser(clientId);
    BigDecimal total =
        cartItems.stream().map(i -> i.pricePerItem().multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new CartSummaryResponse(cartItems, total);
  }

  // POST
  @Transactional
  public void addCartItem(AddToCartRequest request, String clientId) {
    Product product = productRepository.findById(request.productId())
        .orElseThrow(() -> new ProductNotFoundException(request.productId()));
    CartItem cartItem;
    User user = getCurrentUser();
    if (user != null) {
      cartItem = cartItemRepository.findByUserAndProduct(user, product)
          .orElse(new CartItem(null, user, null, product, 0));
    } else if (clientId != null) {
      cartItem = cartItemRepository.findBySessionIdAndProduct(clientId, product)
          .orElse(new CartItem(null, null, clientId, product, 0));
    } else {
      throw new UserNotFoundException();
    }

    if (cartItem.getQuantity() + request.quantity() > product.getStock()) {
      throw new NotEnoughStockException();
    }

    cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
    cartItemRepository.save(cartItem);
  }

  // PATCH
  @Transactional
  public void updateCartItem(Long id, UpdateCartItemRequest request) {
    CartItem cartItem =
        cartItemRepository.findById(id).orElseThrow(ItemNonExistInCartException::new);
    if (request.quantity() > cartItem.getProduct().getStock()) {
      throw new NotEnoughStockException();
    }

    cartItem.setQuantity(request.quantity());
    cartItemRepository.save(cartItem);
  }

  // DELETE
  @Transactional
  public void deleteCartItem(Long id) {
    if (!cartItemRepository.existsById(id)) {
      throw new ItemNonExistInCartException();
    }
    cartItemRepository.deleteById(id);
  }

  @Transactional
  public void clearCartForUser(String clientId) {

    User user = getCurrentUser();
    if (user != null) {
      cartItemRepository.deleteByUser(user);
    } else if (clientId != null) {
      cartItemRepository.deleteBySessionId(clientId);
    } else {
      throw new UserNotFoundException();
    }
  }

  private CartItemResponse mapToResponse(CartItem cartItem) {
    BigDecimal pricePerItem = cartItem.getProduct().getPrice();
    BigDecimal totalPrice = pricePerItem.multiply(new BigDecimal(cartItem.getQuantity()));
    return new CartItemResponse(cartItem.getId(), cartItem.getProduct().getId(),
        cartItem.getProduct().getName(), cartItem.getQuantity(), pricePerItem, totalPrice);
  }

}
