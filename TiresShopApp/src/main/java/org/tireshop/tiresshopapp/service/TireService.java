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
  private final ProductService productService;

  // GET
  public List<TireResponse> getAllTire() {
    return tireRepository.findAll().stream().map(this::mapToResponse).toList();
  }

  public TireResponse getTireById(Long id) {
    Tire tire = tireRepository.findById(id).orElseThrow(() -> new TireNotFoundException(id));
    return mapToResponse(tire);
  }

  public Page<TireResponse> getTires(String name, String season, String size, BigDecimal minPrice, BigDecimal maxPrice, int page, int sizePerPage, String[] sort) {
    Specification<Tire> specification = Specification
            .where(TireSpecifications.hasSeason(season))
            .and(TireSpecifications.hasSize(size))
            .and(TireSpecifications.hasNameContaining(name))
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

    productService.updateProduct(id, request.request());
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
    tireRepository.deleteById(id);

  }

  public TireResponse mapToResponse(Tire tire) {
    return new TireResponse(tire.getId(), tire.getName(), tire.getPrice(), tire.getDescription(),
        tire.getStock(), tire.getType(), tire.getSeason(), tire.getSize());
  }
}
