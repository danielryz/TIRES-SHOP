package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateRimRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateRimRequest;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.entity.Rim;
import org.tireshop.tiresshopapp.exception.RimNotFoundException;
import org.tireshop.tiresshopapp.repository.RimRepository;
import org.tireshop.tiresshopapp.specifications.RimSpecifications;
import org.tireshop.tiresshopapp.util.SortUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RimService {

  private final RimRepository rimRepository;
  private final ProductService productService;

  // GET
  public List<RimResponse> getAllRim() {
    return rimRepository.findAll().stream().map(this::mapToResponse).toList();
  }

  public RimResponse getRimById(Long id) {
    Rim rim = rimRepository.findById(id).orElseThrow(() -> new RimNotFoundException(id));
    return mapToResponse(rim);
  }

  public Page<RimResponse> getRims(String material, String size, String boltPattern, String name, BigDecimal minPrice, BigDecimal maxPrice, int page, int sizePerPage, String[] sort) {
    Specification<Rim> specification = Specification
            .where(RimSpecifications.hasMaterial(material))
            .and(RimSpecifications.hasSize(size))
            .and(RimSpecifications.hasBoltPattern(boltPattern))
            .and(RimSpecifications.hasNameContaining(name))
            .and(RimSpecifications.hasMinPrice(minPrice))
            .and(RimSpecifications.hasMaxPrice(maxPrice));

    Sort sorting = SortUtils.parseSort(sort);
    Pageable pageable = PageRequest.of(page, sizePerPage, sorting);
    Page<Rim> rims = rimRepository.findAll(specification, pageable);
    return rims.map(this::mapToResponse);
  }

  // POST
  @Transactional
  public List<RimResponse> createNewRim(List<CreateRimRequest> requests) {
    List<Rim> rims = requests.stream().map(request -> {
      Rim rim = new Rim();
      rim.setName(request.name());
      rim.setPrice(request.price());
      rim.setDescription(request.description());
      rim.setStock(request.stock());
      rim.setType(request.type());
      rim.setMaterial(request.material());
      rim.setSize(request.size());
      rim.setBoltPattern(request.boltPattern());
      return rim;
    }).toList();

    List<Rim> savedRims = rimRepository.saveAll(rims);

    return savedRims.stream().map(this::mapToResponse).toList();
  }

  // PATCH
  @Transactional
  public void updateRim(Long id, UpdateRimRequest request) {
    Rim rim = rimRepository.findById(id).orElseThrow(() -> new RimNotFoundException(id));

    productService.updateProduct(id, request.request());
    if (request.material() != null && !request.material().isBlank())
      rim.setMaterial(request.material());
    if (request.size() != null && !request.size().isBlank())
      rim.setSize(request.size());

    rimRepository.save(rim);
  }

  // DELETE
  @Transactional
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
