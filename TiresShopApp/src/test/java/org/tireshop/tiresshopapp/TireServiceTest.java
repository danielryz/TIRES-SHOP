package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.dto.request.create.CreateTireRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateTireRequest;
import org.tireshop.tiresshopapp.dto.response.FilterCountResponse;
import org.tireshop.tiresshopapp.dto.response.TireFilterResponse;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.entity.ProductType;
import org.tireshop.tiresshopapp.entity.Tire;
import org.tireshop.tiresshopapp.exception.TireNotFoundException;
import org.tireshop.tiresshopapp.repository.TireRepository;
import org.tireshop.tiresshopapp.service.ImageService;
import org.tireshop.tiresshopapp.service.TireService;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TireServiceTest {

  @Mock
  private TireRepository tireRepository;

  @Mock
  private ImageService imageService;

  @InjectMocks
  private TireService tireService;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnTireById_WhenExists() {
    Tire tire = new Tire();
    tire.setId(1L);
    tire.setName("Tire A");

    when(tireRepository.findById(1L)).thenReturn(Optional.of(tire));

    TireResponse result = tireService.getTireById(1L);

    assertEquals("Tire A", result.name());
    assertEquals(1L, result.id());
  }

  @Test
    void shouldThrowException_WhenTireNotFound() {
        when(tireRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TireNotFoundException.class, () -> tireService.getTireById(1L));
    }

  @Test
  void shouldReturnPaginatedTires() {
    Tire tire1 = new Tire();
    tire1.setName("Tire A");
    Tire tire2 = new Tire();
    tire2.setName("Tire B");

    Page<Tire> pageOfTires = new PageImpl<>(List.of(tire1, tire2));

    when(tireRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(pageOfTires);

    Page<TireResponse> result = tireService.getTires("Winter", List.of("WINTER"),
        List.of("205/55 R16"), BigDecimal.ZERO, BigDecimal.TEN, 0, 10, "price,asc");

    assertEquals(2, result.getTotalElements());
    assertEquals("Tire A", result.getContent().get(0).name());
    assertEquals("Tire B", result.getContent().get(1).name());
  }

  @Test
  void shouldCreateNewTires() {
    CreateTireRequest request = new CreateTireRequest("Tire X", BigDecimal.TEN, "desc", 10,
        ProductType.TIRE, "WINTER", "205/55 R16");
    Tire saved = new Tire();
    saved.setId(1L);
    saved.setName("Tire X");

    when(tireRepository.saveAll(anyList())).thenReturn(List.of(saved));

    List<TireResponse> result = tireService.createNewTire(List.of(request));

    assertEquals(1, result.size());
    assertEquals("Tire X", result.get(0).name());
  }

  @Test
  void shouldUpdateTireFields() {
    Tire tire = new Tire();
    tire.setId(1L);
    tire.setName("Old Tire");

    UpdateTireRequest request = new UpdateTireRequest("New Tire", BigDecimal.ONE, "new desc", 5,
        ProductType.TIRE, "SUMMER", "215/60 R16");

    when(tireRepository.findById(1L)).thenReturn(Optional.of(tire));

    tireService.updateTire(1L, request);

    assertEquals("New Tire", tire.getName());
    assertEquals("SUMMER", tire.getSeason());
    verify(tireRepository).save(tire);
  }

  @Test
    void shouldThrowWhenUpdatingNonExistingTire() {
        when(tireRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateTireRequest request = new UpdateTireRequest("x", BigDecimal.ONE, "d", 1,
                ProductType.TIRE, "WINTER", "205/55 R16");

        assertThrows(TireNotFoundException.class, () -> tireService.updateTire(1L, request));
    }

  @Test
    void shouldDeleteTire_WhenExists() {
        when(tireRepository.existsById(1L)).thenReturn(true);

        tireService.deleteTire(1L);

        verify(imageService).deleteImagesByProductId(1L);
        verify(tireRepository).deleteById(1L);
    }

  @Test
    void shouldThrowWhenDeletingNonExistingTire() {
        when(tireRepository.existsById(1L)).thenReturn(false);

        assertThrows(TireNotFoundException.class, () -> tireService.deleteTire(1L));
    }

  @Test
    void shouldReturnFilterOptions() {
        when(tireRepository.countTiresBySeason()).thenReturn(
                List.of(new FilterCountResponse("SUMMER", 3L)));
        when(tireRepository.countTiresBySize()).thenReturn(
                List.of(new FilterCountResponse("205/55 R16", 2L)));
        when(tireRepository.findMinPrice()).thenReturn(BigDecimal.valueOf(100));
        when(tireRepository.findMaxPrice()).thenReturn(BigDecimal.valueOf(500));

        TireFilterResponse response = tireService.getAvailableFilterOptions();

        assertEquals(1, response.getSeasons().size());
        assertEquals(1, response.getSizes().size());
        assertEquals(BigDecimal.valueOf(100), response.getMinPrice());
        assertEquals(BigDecimal.valueOf(500), response.getMaxPrice());
    }

  @Test
    void shouldReturnDefaultMinMaxPrice_WhenNull() {
        when(tireRepository.countTiresBySeason()).thenReturn(List.of());
        when(tireRepository.countTiresBySize()).thenReturn(List.of());
        when(tireRepository.findMinPrice()).thenReturn(null);
        when(tireRepository.findMaxPrice()).thenReturn(null);

        TireFilterResponse response = tireService.getAvailableFilterOptions();

        assertEquals(BigDecimal.ZERO, response.getMinPrice());
        assertEquals(BigDecimal.valueOf(1000), response.getMaxPrice());
    }
}
