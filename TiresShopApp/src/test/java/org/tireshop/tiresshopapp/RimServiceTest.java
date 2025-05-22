package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.tireshop.tiresshopapp.dto.request.create.CreateRimRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateRimRequest;
import org.tireshop.tiresshopapp.dto.response.FilterCountResponse;
import org.tireshop.tiresshopapp.dto.response.RimFilterResponse;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.entity.ProductType;
import org.tireshop.tiresshopapp.entity.Rim;
import org.tireshop.tiresshopapp.exception.RimNotFoundException;
import org.tireshop.tiresshopapp.repository.RimRepository;
import org.tireshop.tiresshopapp.service.ImageService;
import org.tireshop.tiresshopapp.service.RimService;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RimServiceTest {

  @Mock
  private RimRepository rimRepository;

  @Mock
  private ImageService imageService;

  @InjectMocks
  private RimService rimService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnRimById_WhenExists() {
    Rim rim = new Rim();
    rim.setId(1L);
    rim.setName("Rim A");

    when(rimRepository.findById(1L)).thenReturn(Optional.of(rim));

    RimResponse response = rimService.getRimById(1L);

    assertEquals(1L, response.id());
    assertEquals("Rim A", response.name());
  }

  @Test
    void shouldThrow_WhenRimNotFound() {
        when(rimRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RimNotFoundException.class, () -> rimService.getRimById(1L));
    }

  @Test
  void shouldReturnPaginatedRims() {
    Rim rim = new Rim();
    rim.setName("Rim B");

    Page<Rim> pageOfRim = new PageImpl<>(List.of(rim));

    when(rimRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(pageOfRim);

    Page<RimResponse> result = rimService.getRims(List.of("Aluminum"), List.of("18"),
        List.of("5x112"), "Rim", BigDecimal.ZERO, BigDecimal.TEN, 0, 10, "price,asc");

    assertEquals(1, result.getTotalElements());
    assertEquals("Rim B", result.getContent().get(0).name());
  }

  @Test
  void shouldCreateNewRims() {
    CreateRimRequest req = new CreateRimRequest("Rim C", BigDecimal.valueOf(200), "desc", 5,
        ProductType.RIM, "Steel", "17", "4x100");

    Rim saved = new Rim();
    saved.setId(2L);
    saved.setName("Rim C");

    when(rimRepository.saveAll(anyList())).thenReturn(List.of(saved));

    List<RimResponse> responses = rimService.createNewRim(List.of(req));

    assertEquals(1, responses.size());
    assertEquals(2L, responses.get(0).id());
    assertEquals("Rim C", responses.get(0).name());
  }

  @Test
  void shouldUpdateRimFields() {
    Rim rim = new Rim();
    rim.setId(3L);
    rim.setName("Old");

    UpdateRimRequest req =
        new UpdateRimRequest("New", BigDecimal.ONE, "d", 2, ProductType.RIM, "Alloy", "19", null);

    when(rimRepository.findById(3L)).thenReturn(Optional.of(rim));

    rimService.updateRim(3L, req);

    assertEquals("New", rim.getName());
    assertEquals("Alloy", rim.getMaterial());
    verify(rimRepository).save(rim);
  }

  @Test
    void shouldThrow_WhenUpdatingNonexistentRim() {
        when(rimRepository.findById(1L)).thenReturn(Optional.empty());
        UpdateRimRequest req = new UpdateRimRequest(
                "n", BigDecimal.ONE, "d", 1,
                ProductType.RIM, "m", "s", "b");
        assertThrows(RimNotFoundException.class, () -> rimService.updateRim(1L, req));
    }

  @Test
    void shouldDeleteRim_WhenExists() {
        when(rimRepository.existsById(1L)).thenReturn(true);

        rimService.deleteRim(1L);

        verify(imageService).deleteImagesByProductId(1L);
        verify(rimRepository).deleteById(1L);
    }

  @Test
    void shouldThrow_WhenDeletingNonexistentRim() {
        when(rimRepository.existsById(1L)).thenReturn(false);
        assertThrows(RimNotFoundException.class, () -> rimService.deleteRim(1L));
    }

  @Test
  void shouldReturnFilterOptions() {
    List<FilterCountResponse> mat = List.of(new FilterCountResponse("Steel", 2L));
    List<FilterCountResponse> sz = List.of(new FilterCountResponse("17", 3L));
    List<FilterCountResponse> bp = List.of(new FilterCountResponse("5x112", 4L));

    when(rimRepository.countRimByMaterial()).thenReturn(mat);
    when(rimRepository.countRimBySize()).thenReturn(sz);
    when(rimRepository.countRimByBoltPattern()).thenReturn(bp);
    when(rimRepository.findMinPrice()).thenReturn(BigDecimal.ONE);
    when(rimRepository.findMaxPrice()).thenReturn(BigDecimal.valueOf(500));

    RimFilterResponse response = rimService.getAvailableFilterOptions();

    assertEquals(mat, response.getMaterial());
    assertEquals(sz, response.getSize());
    assertEquals(bp, response.getBoltPattern());
    assertEquals(BigDecimal.ONE, response.getMinPrice());
    assertEquals(BigDecimal.valueOf(500), response.getMaxPrice());
  }

  @Test
    void shouldReturnDefaultMinMaxPrice_WhenNull() {
        when(rimRepository.countRimByMaterial()).thenReturn(Collections.emptyList());
        when(rimRepository.countRimBySize()).thenReturn(Collections.emptyList());
        when(rimRepository.countRimByBoltPattern()).thenReturn(Collections.emptyList());
        when(rimRepository.findMinPrice()).thenReturn(null);
        when(rimRepository.findMaxPrice()).thenReturn(null);

        RimFilterResponse response = rimService.getAvailableFilterOptions();

        assertEquals(BigDecimal.ZERO, response.getMinPrice());
        assertEquals(BigDecimal.valueOf(1000), response.getMaxPrice());
    }
}
