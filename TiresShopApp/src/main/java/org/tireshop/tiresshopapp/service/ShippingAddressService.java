package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateShippingAddressRequest;
import org.tireshop.tiresshopapp.dto.response.ShippingAddressResponse;
import org.tireshop.tiresshopapp.entity.Order;
import org.tireshop.tiresshopapp.entity.ShippingAddress;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.*;
import org.tireshop.tiresshopapp.repository.OrderRepository;
import org.tireshop.tiresshopapp.repository.ShippingAddressRepository;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {
  private final ShippingAddressRepository shippingAddressRepository;
  private final OrderRepository orderRepository;
  private final UserService userService;

  // POST
  @Transactional
  public ShippingAddressResponse addShippingAddressToMyOrder(CreateShippingAddressRequest request,
      String clientId) {
    Order order = getCurrentOrder(clientId);
    if (order.getShippingAddress() != null) {
      throw new ShippingAddressAlreadyInUseException();
    }
    ShippingAddress shippingAddress = new ShippingAddress();
    copyFields(request, shippingAddress);
    shippingAddress.setOrder(order);
    order.setShippingAddress(shippingAddress);

    return mapToResponse(shippingAddressRepository.save(shippingAddress));
  }

  // PATCH
  @Transactional
  public void updateShippingAddress(CreateShippingAddressRequest request, String clientId) {
    Order order = getCurrentOrder(clientId);
    ShippingAddress shippingAddress = order.getShippingAddress();

    if (shippingAddress == null) {
      throw new NoShippingAddressAssignException();
    }
    if (request.street() != null && !request.street().isBlank())
      shippingAddress.setStreet(request.street());
    if(request.houseNumber() != null && !request.houseNumber().isBlank())
      shippingAddress.setHouseNumber(request.houseNumber());
    if(request.apartmentNumber() != null && !request.apartmentNumber().isBlank())
      shippingAddress.setApartmentNumber(request.apartmentNumber());
    if(request.postalCode() != null && !request.postalCode().isBlank())
      shippingAddress.setPostalCode(request.postalCode());
    if (request.city() != null && !request.city().isBlank())
      shippingAddress.setCity(request.city());

    shippingAddressRepository.save(shippingAddress);
  }

  // DELETE
  @Transactional
  public void deleteShippingAddressFromMyOrder(String clientId) {
    Order order = getCurrentOrder(clientId);
    ShippingAddress shippingAddress = order.getShippingAddress();
    if (shippingAddress == null) {
      throw new NoShippingAddressAssignException();
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

  private Order getCurrentOrder(String clientId) {

    User user = userService.getCurrentUser();

    if (user != null) {
      // login user
      return orderRepository.findTopByUserOrderByCreatedAtDesc(user)
          .orElseThrow(() -> new NoOrderActiveForUserException(user));
    } else if (clientId != null) {
      // quest
      return orderRepository.findTopBySessionIdOrderByCreatedAtDesc(clientId)
          .orElseThrow(() -> new NoOrderActiveForUserException(clientId));
    } else {
      throw new UserNotFoundException();
    }
  }

  private ShippingAddressResponse mapToResponse(ShippingAddress address) {
    return new ShippingAddressResponse(address.getId(), address.getStreet(),
        address.getHouseNumber(), address.getApartmentNumber(), address.getPostalCode(),
        address.getCity());
  }
}
