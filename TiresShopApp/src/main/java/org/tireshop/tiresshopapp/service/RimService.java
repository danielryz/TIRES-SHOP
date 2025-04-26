package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateRimRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateRimRequest;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.entity.Rim;
import org.tireshop.tiresshopapp.exception.RimNotFoundException;
import org.tireshop.tiresshopapp.repository.RimRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RimService {

  private final RimRepository rimRepository;

  // GET
  public List<RimResponse> getAllRim() {
    return rimRepository.findAll().stream().map(this::mapToResponse).toList();
  }

  public RimResponse getRimById(Long id) {
    Rim rim = rimRepository.findById(id).orElseThrow(() -> new RimNotFoundException(id));
    return mapToResponse(rim);
  }

  public List<RimResponse> getRimByMaterial(String material) {
    List<RimResponse> rims = rimRepository.findRimByMaterial(material);
    if (rims.isEmpty()) {
      throw new RimNotFoundException(material);
    }
    return rims;
  }

  public List<RimResponse> getRimBySize(String size) {
    List<RimResponse> rims = rimRepository.findRimBySize(size);
    if (rims.isEmpty()) {
      throw new RimNotFoundException(size);
    }
    return rims;
  }

  // POST
  @Transactional
  public RimResponse createNewRim(CreateRimRequest request) {
    Rim rim = new Rim();
    rim.setName(request.name());
    rim.setPrice(request.price());
    rim.setDescription(request.description());
    rim.setStock(request.stock());
    rim.setType(request.type());
    rim.setMaterial(request.material());
    rim.setSize(request.size());
    rim.setBoltPattern(request.boltPattern());

    return mapToResponse(rimRepository.save(rim));
  }

  // PATCH
  @Transactional
  public RimResponse updateRim(Long id, UpdateRimRequest request) {
    Rim rim = rimRepository.findById(id).orElseThrow(() -> new RimNotFoundException(id));

    if (request.name() != null && !request.name().isBlank())
      rim.setName(request.name());
    if (request.price() != null)
      rim.setPrice(request.price());
    if (request.description() != null && !request.description().isBlank())
      rim.setDescription(request.description());
    if (request.stock() >= 0)
      rim.setStock(request.stock());
    if (request.type() != null)
      rim.setType(request.type());
    if (request.material() != null && !request.material().isBlank())
      rim.setMaterial(request.material());
    if (request.size() != null)
      rim.setSize(request.size());

    return mapToResponse(rimRepository.save(rim));
  }

  // DELETE
  public void deleteRim(Long id) {
    if (!rimRepository.existsById(id)) {
      throw new RimNotFoundException(id);
    }
    rimRepository.deleteById(id);

  }

  private RimResponse mapToResponse(Rim rim) {
    return new RimResponse(rim.getId(), rim.getName(), rim.getPrice(), rim.getDescription(),
        rim.getStock(), rim.getType(), rim.getMaterial(), rim.getSize(), rim.getBoltPattern());
  }
}
