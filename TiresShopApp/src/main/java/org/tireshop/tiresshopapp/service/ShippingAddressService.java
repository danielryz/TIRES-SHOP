package org.tireshop.tiresshopapp.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateShippingAddressRequest;
import org.tireshop.tiresshopapp.entity.Order;
import org.tireshop.tiresshopapp.entity.ShippingAddress;
import org.tireshop.tiresshopapp.repository.OrderRepository;
import org.tireshop.tiresshopapp.repository.ShippingAddressRepository;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {
  private final ShippingAddressRepository shippingAddressRepository;
  private final OrderRepository orderRepository;
  private final UserService userService;
  private final HttpSession session;

  // POST
  @Transactional
  public void addShippingAddressToMyOrder(CreateShippingAddressRequest request) {
    Order order = getCurrentOrder();
    if (order.getShippingAddress() != null) {
      throw new RuntimeException("Address already in use in order.");
    }
    ShippingAddress shippingAddress = new ShippingAddress();
    copyFields(request, shippingAddress);
    shippingAddress.setOrder(order);
    order.setShippingAddress(shippingAddress);

    shippingAddressRepository.save(shippingAddress);
  }

  // PATCH
  @Transactional
  public void updateShippingAddress(CreateShippingAddressRequest request) {
    Order order = getCurrentOrder();
    ShippingAddress shippingAddress = order.getShippingAddress();

    if (shippingAddress == null) {
      throw new RuntimeException("No shipping address assigned to this order.");
    }
    updateFieldIfPresent(request.street(), shippingAddress::setStreet);
    updateFieldIfPresent(request.houseNumber(), shippingAddress::setHouseNumber);
    updateFieldIfPresent(request.apartmentNumber(), shippingAddress::setApartmentNumber);
    updateFieldIfPresent(request.postalCode(), shippingAddress::setPostalCode);
    updateFieldIfPresent(request.city(), shippingAddress::setCity);

    shippingAddressRepository.save(shippingAddress);
  }

  // DELETE
  @Transactional
  public void deleteShippingAddressFromMyOrder() {
    Order order = getCurrentOrder();
    ShippingAddress shippingAddress = order.getShippingAddress();
    if (shippingAddress == null) {
      throw new RuntimeException("No shipping address assigned to this order.");
    }
    order.setShippingAddress(null);
    shippingAddressRepository.delete(shippingAddress);
  }

  private void copyFields(CreateShippingAddressRequest request, ShippingAddress shippingAddress) {
    shippingAddress.setStreet(request.street());
    shippingAddress.setHouseNumber(request.houseNumber());
    shippingAddress.setApartmentNumber(request.apartmentNumber());
    shippingAddress.setPostalCode(request.postalCode());
    shippingAddress.setCity(request.city());
  }

  private void updateFieldIfPresent(String newValue, Consumer<String> setter) {
    if (newValue != null && !newValue.isBlank()) {
      setter.accept(newValue);
    }
  }

  private Order getCurrentOrder() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
      // login user
      return orderRepository.findTopByUserOrderByCreatedAtDesc(userService.getCurrentUser())
          .orElseThrow(() -> new RuntimeException(
              "No active order found for user " + userService.getCurrentUser()));
    } else {
      // quest
      String sessionId = session.getId();
      return orderRepository.findTopBySessionIdOrderByCreatedAtDesc(sessionId).orElseThrow(
          () -> new RuntimeException("No active order found for session id: " + sessionId));
    }
  }
}
