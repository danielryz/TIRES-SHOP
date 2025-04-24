package org.tireshop.tiresshopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateAddressRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAddressRequest;
import org.tireshop.tiresshopapp.dto.response.AddressResponse;
import org.tireshop.tiresshopapp.entity.Address;
import org.tireshop.tiresshopapp.entity.AddressType;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.repository.AddressRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;
  private final UserService userService;

  public User getCurrentUser() {
    return userService.getCurrentUser();
  }

  // GET
  public List<AddressResponse> getCurrentUserAllAddresses() {
    User user = getCurrentUser();
    return addressRepository.findByUser(user).stream().map(this::mapToResponse).toList();
  }

  public AddressResponse getAddressById(Long id) {
    User user = getCurrentUser();
    Address address = addressRepository.findByIdAndUser(id, user)
        .orElseThrow(() -> new RuntimeException("Adres nie istnieje"));
    return mapToResponse(address);
  }

  public List<AddressResponse> getAddressesByType(AddressType addressType) {
    User user = getCurrentUser();
    return addressRepository.findAddressesByTypeAndUser(addressType, user).stream()
        .map(this::mapToResponse).toList();
  }

  // POST
  public AddressResponse addAddress(CreateAddressRequest request) {
    User user = getCurrentUser();
    Address address = new Address();
    address.setStreet(request.street());
    address.setHouseNumber(request.houseNumber());
    address.setApartmentNumber(request.apartmentNumber());
    address.setPostalCode(request.postalCode());
    address.setCity(request.city());
    address.setUser(user);
    return mapToResponse(addressRepository.save(address));
  }

  // PATCH
  public AddressResponse updateAddress(Long id, UpdateAddressRequest request) {
    User user = getCurrentUser();
    Address address = addressRepository.findByIdAndUser(id, user)
        .orElseThrow(() -> new RuntimeException("Adres nie znaleziony"));
    if (request.street() != null && !request.street().isBlank())
      address.setStreet(request.street());
    if (request.houseNumber() != null && !request.houseNumber().isBlank())
      address.setHouseNumber(request.houseNumber());
    if (request.apartmentNumber() != null && !request.apartmentNumber().isBlank())
      address.setApartmentNumber(request.apartmentNumber());
    if (request.postalCode() != null && !request.postalCode().isBlank())
      address.setPostalCode(request.postalCode());
    if (request.city() != null && !request.city().isBlank())
      address.setCity(request.city());

    return mapToResponse(addressRepository.save(address));
  }

  // DELETE
  public void deleteAddress(Long id) {
    User user = getCurrentUser();
    Address address = addressRepository.findByIdAndUser(id, user)
        .orElseThrow(() -> new RuntimeException("Adres nie istnieje"));
    addressRepository.delete(address);
  }

  private AddressResponse mapToResponse(Address address) {
    return new AddressResponse(address.getId(), address.getStreet(), address.getHouseNumber(),
        address.getApartmentNumber(), address.getPostalCode(), address.getCity(),
        address.getType());
  }


}
