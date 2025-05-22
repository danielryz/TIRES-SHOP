package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateAddressRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAddressRequest;
import org.tireshop.tiresshopapp.dto.response.AddressResponse;
import org.tireshop.tiresshopapp.entity.Address;
import org.tireshop.tiresshopapp.entity.AddressType;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.AddressNotFoundException;
import org.tireshop.tiresshopapp.repository.AddressRepository;
import org.tireshop.tiresshopapp.service.AddressService;
import org.tireshop.tiresshopapp.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

  @Mock
  private AddressRepository addressRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private AddressService addressService;

  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(1L);
    when(userService.getCurrentUser()).thenReturn(user);
  }

  @Test
  void shouldReturnAllUserAddresses() {
    Address address = new Address();
    address.setId(1L);
    address.setCity("Warsaw");

    when(addressRepository.findByUser(user)).thenReturn(List.of(address));

    List<AddressResponse> result = addressService.getCurrentUserAllAddresses();

    assertEquals(1, result.size());
    assertEquals("Warsaw", result.get(0).city());
  }

  @Test
  void shouldReturnAddressById() {
    Address address = new Address();
    address.setId(2L);
    address.setUser(user);
    address.setCity("Krakow");

    when(addressRepository.findByIdAndUser(2L, user)).thenReturn(Optional.of(address));

    AddressResponse result = addressService.getAddressById(2L);

    assertEquals("Krakow", result.city());
  }

  @Test
    void shouldThrowWhenAddressNotFoundById() {
        when(addressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class, () -> addressService.getAddressById(1L));
    }

  @Test
  void shouldReturnAddressesByType() {
    Address address = new Address();
    address.setType(AddressType.BILLING);
    address.setCity("Gdansk");

    when(addressRepository.findAddressesByTypeAndUser(AddressType.BILLING, user))
        .thenReturn(List.of(address));

    List<AddressResponse> result = addressService.getAddressesByType(AddressType.BILLING);

    assertEquals(1, result.size());
    assertEquals("Gdansk", result.get(0).city());
  }

  @Test
    void shouldThrowWhenNoAddressByType() {
        when(addressRepository.findAddressesByTypeAndUser(AddressType.SHIPPING, user))
                .thenReturn(Collections.emptyList());

        assertThrows(AddressNotFoundException.class,
                () -> addressService.getAddressesByType(AddressType.SHIPPING));
    }

  @Test
  void shouldAddAddressSuccessfully() {
    CreateAddressRequest request =
        new CreateAddressRequest("Street", "1", "2", "00-001", "Poznan", AddressType.BILLING);

    ArgumentCaptor<Address> captor = ArgumentCaptor.forClass(Address.class);
    Address savedAddress = new Address();
    savedAddress.setId(10L);
    savedAddress.setCity("Poznan");

    when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);

    AddressResponse result = addressService.addAddress(request);

    verify(addressRepository).save(captor.capture());
    Address captured = captor.getValue();

    assertEquals("Poznan", captured.getCity());
    assertEquals("Poznan", result.city());
    assertEquals(10L, result.id());
  }

  @Test
  void shouldUpdateAddressFields() {
    Address address = new Address();
    address.setId(1L);
    address.setUser(user);
    address.setCity("OldCity");

    UpdateAddressRequest request =
        new UpdateAddressRequest("NewStreet", "1", "2", "00-001", "NewCity");

    when(addressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(address));

    addressService.updateAddress(1L, request);

    assertEquals("NewStreet", address.getStreet());
    assertEquals("NewCity", address.getCity());
    assertEquals("1", address.getHouseNumber());
    assertEquals("2", address.getApartmentNumber());
    assertEquals("00-001", address.getPostalCode());
    verify(addressRepository).save(address);
  }

  @Test
    void shouldThrowWhenUpdatingNonexistentAddress() {
        when(addressRepository.findByIdAndUser(anyLong(), eq(user))).thenReturn(Optional.empty());
        UpdateAddressRequest request = new UpdateAddressRequest("a", "b", "c", "d", "e");

        assertThrows(AddressNotFoundException.class, () -> addressService.updateAddress(1L, request));
    }

  @Test
  void shouldDeleteAddress() {
    Address address = new Address();
    address.setId(1L);
    address.setUser(user);

    when(addressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(address));

    addressService.deleteAddress(1L);

    verify(addressRepository).delete(address);
  }

  @Test
    void shouldThrowWhenDeletingNonexistentAddress() {
        when(addressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> addressService.deleteAddress(1L));
    }
}
