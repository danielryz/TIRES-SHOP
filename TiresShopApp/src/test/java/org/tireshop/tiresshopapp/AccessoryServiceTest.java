package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.dto.request.create.CreateAccessoryRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAccessoryRequest;
import org.tireshop.tiresshopapp.dto.response.AccessoryFilterCountResponse;
import org.tireshop.tiresshopapp.dto.response.AccessoryFilterResponse;
import org.tireshop.tiresshopapp.dto.response.AccessoryResponse;
import org.tireshop.tiresshopapp.entity.Accessory;
import org.tireshop.tiresshopapp.entity.AccessoryType;
import org.tireshop.tiresshopapp.entity.ProductType;
import org.tireshop.tiresshopapp.exception.AccessoryNotFoundException;
import org.tireshop.tiresshopapp.repository.AccessoryRepository;
import org.tireshop.tiresshopapp.service.AccessoryService;
import org.tireshop.tiresshopapp.service.ImageService;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccessoryServiceTest {

  @Mock
  private AccessoryRepository accessoryRepository;

  @Mock
  private ImageService imageService;

  @InjectMocks
  private AccessoryService accessoryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnAccessoryById_WhenExists() {
    Accessory accessory = new Accessory();
    accessory.setId(1L);
    accessory.setName("Test accessory");

    when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));

    AccessoryResponse result = accessoryService.getAccessoryById(1L);

    assertEquals("Test accessory", result.name());
    assertEquals(1L, result.id());
  }

 @Test
 void shouldThrowException_WhenAccessoryNotFound() {
 when(accessoryRepository.findById(1L)).thenReturn(Optional.empty());
 assertThrows(AccessoryNotFoundException.class, () -> accessoryService.getAccessoryById(1L));
 }

  @Test
  void shouldReturnPaginatedAccessories() {
    Accessory accessory = new Accessory();
    accessory.setName("Cable");

    Page<Accessory> pageOfAccessory = new PageImpl<>(List.of(accessory));

    when(accessoryRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(pageOfAccessory);

    Page<AccessoryResponse> result = accessoryService.getAccessory(List.of(AccessoryType.JACK),
        "Cable", BigDecimal.ZERO, BigDecimal.TEN, 0, 10, "price,asc");

    assertEquals(1, result.getTotalElements());
    assertEquals("Cable", result.getContent().get(0).name());
  }

  @Test
  void shouldCreateNewAccessories() {
    CreateAccessoryRequest request = new CreateAccessoryRequest("Name", BigDecimal.TEN, "desc", 5,
        ProductType.ACCESSORY, AccessoryType.CHAINS);
    Accessory saved = new Accessory();
    saved.setId(1L);
    saved.setName("Name");

    when(accessoryRepository.saveAll(anyList())).thenReturn(List.of(saved));

    List<AccessoryResponse> result = accessoryService.createNewAccessory(List.of(request));

    assertEquals(1, result.size());
    assertEquals("Name", result.get(0).name());
  }

  @Test
  void shouldUpdateAccessoryFields() {
    Accessory accessory = new Accessory();
    accessory.setId(1L);
    accessory.setName("OldName");

    UpdateAccessoryRequest request = new UpdateAccessoryRequest("NewName", BigDecimal.ONE, "desc",
        10, ProductType.ACCESSORY, AccessoryType.CHAINS);

    when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));

    accessoryService.updateAccessory(1L, request);

    assertEquals("NewName", accessory.getName());
    verify(accessoryRepository).save(accessory);
  }

 @Test
 void shouldThrowWhenUpdatingNonExistingAccessory() {
 when(accessoryRepository.findById(1L)).thenReturn(Optional.empty());

 UpdateAccessoryRequest request = new UpdateAccessoryRequest("x", BigDecimal.ONE, "d", 1,
 ProductType.ACCESSORY, AccessoryType.CHAINS);

 assertThrows(AccessoryNotFoundException.class, () -> accessoryService.updateAccessory(1L,
 request));
 }

 @Test
 void shouldDeleteAccessory_WhenExists() {
 when(accessoryRepository.existsById(1L)).thenReturn(true);

 accessoryService.deleteAccessory(1L);

 verify(imageService).deleteImagesByProductId(1L);
 verify(accessoryRepository).deleteById(1L);
 }

 @Test
 void shouldThrowWhenDeletingNonExistingAccessory() {
 when(accessoryRepository.existsById(1L)).thenReturn(false);

 assertThrows(AccessoryNotFoundException.class, () -> accessoryService.deleteAccessory(1L));
 }

 @Test
 void shouldReturnAccessoryFilters() {
 when(accessoryRepository.countAccessoryByType()).thenReturn(
 List.of(new AccessoryFilterCountResponse(AccessoryType.CHAINS, 3L)));
 when(accessoryRepository.findMinPrice()).thenReturn(BigDecimal.ONE);
 when(accessoryRepository.findMaxPrice()).thenReturn(BigDecimal.TEN);

 AccessoryFilterResponse response = accessoryService.getAvailableFilterOptions();

 assertEquals(1, response.getAccessoryType().size());
 assertEquals(BigDecimal.ONE, response.getMinPrice());
 assertEquals(BigDecimal.TEN, response.getMaxPrice());
 }

 @Test
 void shouldReturnDefaultMinMaxPrice_WhenNull() {
 when(accessoryRepository.countAccessoryByType()).thenReturn(List.of());
 when(accessoryRepository.findMinPrice()).thenReturn(null);
 when(accessoryRepository.findMaxPrice()).thenReturn(null);

 AccessoryFilterResponse response = accessoryService.getAvailableFilterOptions();

 assertEquals(BigDecimal.ZERO, response.getMinPrice());
 assertEquals(BigDecimal.valueOf(1000), response.getMaxPrice());
 }
}
