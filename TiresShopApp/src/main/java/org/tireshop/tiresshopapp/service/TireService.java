package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateTireRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateTireRequest;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.entity.Tire;
import org.tireshop.tiresshopapp.exception.TireNotFoundException;
import org.tireshop.tiresshopapp.repository.TireRepository;

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

  public List<TireResponse> getTireBySeason(String seasonName) {
    List<TireResponse> tires = tireRepository.findTireBySeason(seasonName);
    if (tires.isEmpty()) {
      throw new TireNotFoundException(seasonName);
    }
    return tires;
  }

  public List<TireResponse> getTireBySize(String sizeName) {
    List<TireResponse> tires = tireRepository.findTireBySize(sizeName);
    if (tires.isEmpty()) {
      throw new TireNotFoundException(sizeName);
    }
    return tires;
  }

  // POST
  @Transactional
  public TireResponse createNewTire(CreateTireRequest request) {
    Tire tire = new Tire();
    tire.setName(request.name());
    tire.setPrice(request.price());
    tire.setDescription(request.description());
    tire.setStock(request.stock());
    tire.setType(request.type());
    tire.setSeason(request.season());
    tire.setSize(request.size());

    return mapToResponse(tireRepository.save(tire));
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
