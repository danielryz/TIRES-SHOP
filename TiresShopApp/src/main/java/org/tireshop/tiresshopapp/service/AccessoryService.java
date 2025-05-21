package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateAccessoryRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAccessoryRequest;
import org.tireshop.tiresshopapp.dto.response.*;
import org.tireshop.tiresshopapp.entity.Accessory;
import org.tireshop.tiresshopapp.entity.AccessoryType;
import org.tireshop.tiresshopapp.exception.AccessoryNotFoundException;
import org.tireshop.tiresshopapp.repository.AccessoryRepository;
import org.tireshop.tiresshopapp.specifications.AccessorySpecification;
import org.tireshop.tiresshopapp.util.SortUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessoryService {

  private final AccessoryRepository accessoryRepository;
  private final ImageService imageService;
  // GET

  public AccessoryResponse getAccessoryById(Long id) {
    Accessory accessory =
        accessoryRepository.findById(id).orElseThrow(() -> new AccessoryNotFoundException(id));
    return mapToResponse(accessory);
  }

  public Page<AccessoryResponse> getAccessory(List<AccessoryType> accessoryType, String name,
      BigDecimal minPrice, BigDecimal maxPrice, int page, int sizePerPage, String sort) {
    Specification<Accessory> specification =
        Specification.where(AccessorySpecification.hasAccessoryType(accessoryType))
            .and(AccessorySpecification.hasNameContaining(name))
            .and(AccessorySpecification.hasMinPrice(minPrice))
            .and(AccessorySpecification.hasMaxPrice(maxPrice));

    Sort sorting = SortUtils.parseSort(sort);
    Pageable pageable = PageRequest.of(page, sizePerPage, sorting);
    Page<Accessory> accessories = accessoryRepository.findAll(specification, pageable);

    return accessories.map(this::mapToResponse);
  }

  // POST
  @Transactional
  public List<AccessoryResponse> createNewAccessory(List<CreateAccessoryRequest> requests) {
    List<Accessory> accessories = requests.stream().map(request -> {
      Accessory accessory = new Accessory();
      accessory.setName(request.name());
      accessory.setPrice(request.price());
      accessory.setDescription(request.description());
      accessory.setStock(request.stock());
      accessory.setType(request.type());
      accessory.setAccessoryType(request.accessoryType());
      return accessory;
    }).toList();
    List<Accessory> savedAccessories = accessoryRepository.saveAll(accessories);

    return savedAccessories.stream().map(this::mapToResponse).toList();
  }

  // PATCH
  @Transactional
  public void updateAccessory(Long id, UpdateAccessoryRequest request) {
    Accessory accessory =
        accessoryRepository.findById(id).orElseThrow(() -> new AccessoryNotFoundException(id));

    if (request.name() != null && !request.name().isBlank()) {
      accessory.setName(request.name());
    }
    if (request.price() != null) {
      accessory.setPrice(request.price());
    }
    if (request.description() != null && !request.description().isBlank()) {
      accessory.setDescription(request.description());
    }
    if (request.stock() != null) {
      accessory.setStock(request.stock());
    }
    if (request.type() != null) {
      accessory.setType(request.type());
    }
    if (request.accessoryType() != null) {
      accessory.setAccessoryType(request.accessoryType());
    }

    accessoryRepository.save(accessory);
  }

  // DELETE
  @Transactional
  public void deleteAccessory(Long id) {
    if (!accessoryRepository.existsById(id)) {
      throw new AccessoryNotFoundException(id);
    }
    imageService.deleteImagesByProductId(id);
    accessoryRepository.deleteById(id);
  }

  public AccessoryFilterResponse getAvailableFilterOptions() {
    List<AccessoryFilterCountResponse> accessoryWithCount =
        accessoryRepository.countAccessoryByType();
    BigDecimal minPrice = accessoryRepository.findMinPrice();
    BigDecimal maxPrice = accessoryRepository.findMaxPrice();
    return new AccessoryFilterResponse(accessoryWithCount,
        minPrice != null ? minPrice : BigDecimal.ZERO,
        maxPrice != null ? maxPrice : BigDecimal.valueOf(1000));
  }

  private AccessoryResponse mapToResponse(Accessory accessory) {
    return new AccessoryResponse(accessory.getId(), accessory.getName(), accessory.getPrice(),
        accessory.getDescription(), accessory.getStock(), accessory.getType(),
        accessory.getAccessoryType());
  }
}
