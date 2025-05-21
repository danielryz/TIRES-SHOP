package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateTireRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateTireRequest;
import org.tireshop.tiresshopapp.dto.response.FilterCountResponse;
import org.tireshop.tiresshopapp.dto.response.TireFilterResponse;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.entity.Tire;
import org.tireshop.tiresshopapp.exception.TireNotFoundException;
import org.tireshop.tiresshopapp.repository.TireRepository;
import org.tireshop.tiresshopapp.specifications.TireSpecifications;
import org.tireshop.tiresshopapp.util.SortUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TireService {

  private final TireRepository tireRepository;
  private final ImageService imageService;
  // GET

  public TireResponse getTireById(Long id) {
    Tire tire = tireRepository.findById(id).orElseThrow(() -> new TireNotFoundException(id));
    return mapToResponse(tire);
  }

  public Page<TireResponse> getTires(String name, List<String> seasons, List<String> sizes,
      BigDecimal minPrice, BigDecimal maxPrice, int page, int sizePerPage, String sort) {

    Specification<Tire> specification = Specification.where(TireSpecifications.hasSeasons(seasons))
        .and(TireSpecifications.hasSizes(sizes)).and(TireSpecifications.hasNameContaining(name))
        .and(TireSpecifications.hasMinPrice(minPrice))
        .and(TireSpecifications.hasMaxPrice(maxPrice));

    Sort sorting = SortUtils.parseSort(sort);
    Pageable pageable = PageRequest.of(page, sizePerPage, sorting);

    Page<Tire> tires = tireRepository.findAll(specification, pageable);
    return tires.map(this::mapToResponse);
  }


  // POST
  @Transactional
  public List<TireResponse> createNewTire(List<CreateTireRequest> requests) {
    List<Tire> tires = requests.stream().map(request -> {
      Tire tire = new Tire();
      tire.setName(request.name());
      tire.setPrice(request.price());
      tire.setDescription(request.description());
      tire.setStock(request.stock());
      tire.setType(request.type());
      tire.setSeason(request.season());
      tire.setSize(request.size());
      return tire;
    }).toList();
    List<Tire> savedTires = tireRepository.saveAll(tires);

    return savedTires.stream().map(this::mapToResponse).toList();
  }

  // PATCH
  @Transactional
  public void updateTire(Long id, UpdateTireRequest request) {
    Tire tire = tireRepository.findById(id).orElseThrow(() -> new TireNotFoundException(id));

    if (request.name() != null && !request.name().isBlank()) {
      tire.setName(request.name());
    }
    if (request.price() != null) {
      tire.setPrice(request.price());
    }
    if (request.description() != null && !request.description().isBlank()) {
      tire.setDescription(request.description());
    }
    if (request.stock() != null) {
      tire.setStock(request.stock());
    }
    if (request.type() != null) {
      tire.setType(request.type());
    }
    if (request.season() != null && !request.season().isBlank())
      tire.setSeason(request.season());
    if (request.size() != null && !request.size().isBlank())
      tire.setSize(request.size());
    tireRepository.save(tire);
  }

  // DELETE
  @Transactional
  public void deleteTire(Long id) {
    if (!tireRepository.existsById(id)) {
      throw new TireNotFoundException(id);
    }
    imageService.deleteImagesByProductId(id);
    tireRepository.deleteById(id);

  }

  public TireFilterResponse getAvailableFilterOptions() {
    List<FilterCountResponse> seasonsWithCount = tireRepository.countTiresBySeason();
    List<FilterCountResponse> sizesWithCount = tireRepository.countTiresBySize();
    BigDecimal minPrice = tireRepository.findMinPrice();
    BigDecimal maxPrice = tireRepository.findMaxPrice();
    return new TireFilterResponse(seasonsWithCount, sizesWithCount,
        minPrice != null ? minPrice : BigDecimal.ZERO,
        maxPrice != null ? maxPrice : BigDecimal.valueOf(1000));
  }

  public TireResponse mapToResponse(Tire tire) {
    return new TireResponse(tire.getId(), tire.getName(), tire.getPrice(), tire.getDescription(),
        tire.getStock(), tire.getType(), tire.getSeason(), tire.getSize());
  }
}
