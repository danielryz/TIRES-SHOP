package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateAccessoryRequest;
import org.tireshop.tiresshopapp.dto.response.AccessoryResponse;
import org.tireshop.tiresshopapp.entity.Accessory;
import org.tireshop.tiresshopapp.exception.AccessoryNotFoundException;
import org.tireshop.tiresshopapp.repository.AccessoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessoryService {

  private final AccessoryRepository accessoryRepository;

  // GET
  public List<AccessoryResponse> getAllAccessory() {
    return accessoryRepository.findAll().stream().map(this::mapToResponse).toList();
  }

  public AccessoryResponse getAccessoryById(Long id) {
    Accessory accessory =
        accessoryRepository.findById(id).orElseThrow(() -> new AccessoryNotFoundException(id));
    return mapToResponse(accessory);
  }

  public List<AccessoryResponse> getAccessoryByAccessoryType(String accessoryType) {
    List<AccessoryResponse> accessory =
        accessoryRepository.findAccessoryByAccessoryType(accessoryType);
    if (accessory.isEmpty()) {
      throw new AccessoryNotFoundException(accessoryType);
    }
    return accessory;
  }

  // POST
  @Transactional
  public AccessoryResponse createNewAccessory(CreateAccessoryRequest request) {
    Accessory accessory = new Accessory();
    accessory.setName(request.name());
    accessory.setPrice(request.price());
    accessory.setDescription(request.description());
    accessory.setStock(request.stock());
    accessory.setType(request.type());
    accessory.setAccessoryType(request.accessoryType());

    return mapToResponse(accessoryRepository.save(accessory));
  }

  // PATCH
  @Transactional
  public AccessoryResponse updateAccessory(Long id, CreateAccessoryRequest request) {
    Accessory accessory =
        accessoryRepository.findById(id).orElseThrow(() -> new AccessoryNotFoundException(id));

    if (request.name() != null && !request.name().isBlank())
      accessory.setName(request.name());
    if (request.price() != null)
      accessory.setPrice(request.price());
    if (request.description() != null && !request.description().isBlank())
      accessory.setDescription(request.description());
    if (request.stock() >= 0)
      accessory.setStock(request.stock());
    if (request.type() != null)
      accessory.setType(request.type());
    if (request.accessoryType() != null)
      accessory.setAccessoryType(request.accessoryType());

    return mapToResponse(accessoryRepository.save(accessory));
  }

  // DELETE
  @Transactional
  public void deleteAccessory(Long id) {
    if (!accessoryRepository.existsById(id)) {
      throw new AccessoryNotFoundException(id);
    }
    accessoryRepository.deleteById(id);
  }

  private AccessoryResponse mapToResponse(Accessory accessory) {
    return new AccessoryResponse(accessory.getId(), accessory.getName(), accessory.getPrice(),
        accessory.getDescription(), accessory.getStock(), accessory.getType(),
        accessory.getAccessoryType());
  }
}
