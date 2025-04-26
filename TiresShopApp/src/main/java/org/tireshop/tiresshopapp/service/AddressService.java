package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateAddressRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAddressRequest;
import org.tireshop.tiresshopapp.dto.response.AddressResponse;
import org.tireshop.tiresshopapp.entity.Address;
import org.tireshop.tiresshopapp.entity.AddressType;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.AddressNotFoundException;
import org.tireshop.tiresshopapp.repository.AddressRepository;

import java.util.List;
import java.util.function.Consumer;

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
    Address address =
        addressRepository.findByIdAndUser(id, user).orElseThrow(AddressNotFoundException::new);
    return mapToResponse(address);
  }

  public List<AddressResponse> getAddressesByType(AddressType addressType) {
    User user = getCurrentUser();
    List<Address> addresses = addressRepository.findAddressesByTypeAndUser(addressType, user);
    if (addresses.isEmpty()) {
      throw new AddressNotFoundException();
    }
    return addresses.stream().map(this::mapToResponse).toList();
  }

  // POST
  @Transactional
  public void addAddress(CreateAddressRequest request) {
    User user = getCurrentUser();
    Address address = new Address();
    address.setStreet(request.street());
    address.setHouseNumber(request.houseNumber());
    address.setApartmentNumber(request.apartmentNumber());
    address.setPostalCode(request.postalCode());
    address.setCity(request.city());
    address.setUser(user);
    addressRepository.save(address);
  }

  // PATCH
  @Transactional
  public void updateAddress(Long id, UpdateAddressRequest request) {
    User user = getCurrentUser();
    Address address =
        addressRepository.findByIdAndUser(id, user).orElseThrow(AddressNotFoundException::new);
    updateFieldIfPresent(request.street(), address::setStreet);
    updateFieldIfPresent(request.houseNumber(), address::setHouseNumber);
    updateFieldIfPresent(request.apartmentNumber(), address::setApartmentNumber);
    updateFieldIfPresent(request.postalCode(), address::setPostalCode);
    updateFieldIfPresent(request.city(), address::setCity);

    addressRepository.save(address);
  }

  // DELETE
  @Transactional
  public void deleteAddress(Long id) {
    User user = getCurrentUser();
    Address address =
        addressRepository.findByIdAndUser(id, user).orElseThrow(AddressNotFoundException::new);
    addressRepository.delete(address);
  }

  private AddressResponse mapToResponse(Address address) {
    return new AddressResponse(address.getId(), address.getStreet(), address.getHouseNumber(),
        address.getApartmentNumber(), address.getPostalCode(), address.getCity(),
        address.getType());
  }

  private void updateFieldIfPresent(String newValue, Consumer<String> setter) {
    if (newValue != null && !newValue.isBlank()) {
      setter.accept(newValue);
    }
  }



}
