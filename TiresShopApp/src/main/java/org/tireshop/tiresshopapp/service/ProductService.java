package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.tireshop.tiresshopapp.dto.request.create.CreateProductRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateProductRequest;
import org.tireshop.tiresshopapp.dto.response.*;
import org.tireshop.tiresshopapp.entity.*;
import org.tireshop.tiresshopapp.exception.ProductNotFoundException;
import org.tireshop.tiresshopapp.repository.ProductRepository;
import org.tireshop.tiresshopapp.specifications.ProductSpecifications;
import org.tireshop.tiresshopapp.util.SortUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ImageService imageService;

  public BaseProductResponse getProductById(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    return mapToResponse(product);
  }

  public Page<BaseProductResponse> getProducts(String name, BigDecimal minPrice,
      BigDecimal maxPrice, ProductType type, int page, int sizePerPage, String sort) {
    Specification<Product> specification =
        Specification.where(ProductSpecifications.hasNameContaining(name))
            .and(ProductSpecifications.hasMinPrice(minPrice))
            .and(ProductSpecifications.hasMaxPrice(maxPrice))
            .and(ProductSpecifications.hasProductType(type));

    Sort sorting = SortUtils.parseSort(sort);

    Pageable pageable = PageRequest.of(page, sizePerPage, sorting);
    Page<Product> products = productRepository.findAll(specification, pageable);

    return products.map(this::mapToResponse);
  }

  public Page<BaseProductResponse> searchProducts(String keyword, int page, int sizePerPage,
      String sort) {
    Specification<Product> specification =
        Specification.where(ProductSpecifications.hasKeywordContaining(keyword));

    Sort sorting = SortUtils.parseSort(sort);

    Pageable pageable = PageRequest.of(page, sizePerPage, sorting);
    Page<Product> products = productRepository.findAll(specification, pageable);

    return products.map(this::mapToResponse);
  }

  // POST
  @Transactional
  public List<BaseProductResponse> createProduct(List<CreateProductRequest> requests) {
    List<Product> products = requests.stream().map(request -> {
      Product product = new Product();
      product.setName(request.name());
      product.setPrice(request.price());
      product.setDescription(request.description());
      product.setStock(request.stock());
      product.setType(request.type());
      return product;
    }).toList();
    List<Product> savedProducts = productRepository.saveAll(products);

    return savedProducts.stream().map(this::mapToResponse).toList();
  }

  // PATCH
  @Transactional
  public void updateProduct(Long id, UpdateProductRequest request) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    if (request.name() != null && !request.name().isBlank())
      product.setName(request.name());
    if (request.price() != null)
      product.setPrice(request.price());
    if (request.description() != null && !request.description().isBlank())
      product.setDescription(request.description());
    if (request.stock() >= 0)
      product.setStock(request.stock());
    if (request.type() != null)
      product.setType(request.type());

    productRepository.save(product);
  }

  // DELETE
  @Transactional
  public void deleteProduct(Long id) {
    if (!productRepository.existsById(id)) {
      throw new ProductNotFoundException(id);
    }
    imageService.deleteImagesByProductId(id);
    productRepository.deleteById(id);
  }

  private BaseProductResponse mapToResponse(Product product) {

    if (product instanceof Tire tire) {
      return new TireResponse(tire.getId(), tire.getName(), tire.getPrice(), tire.getDescription(),
          tire.getStock(), tire.getType(), tire.getSeason(), tire.getSize());
    } else if (product instanceof Rim rim) {
      return new RimResponse(rim.getId(), rim.getName(), rim.getPrice(), rim.getDescription(),
          rim.getStock(), rim.getType(), rim.getMaterial(), rim.getSize(), rim.getBoltPattern());
    } else if (product instanceof Accessory accessory) {
      return new AccessoryResponse(accessory.getId(), accessory.getName(), accessory.getPrice(),
          accessory.getDescription(), accessory.getStock(), accessory.getType(),
          accessory.getAccessoryType());
    } else
      return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
          product.getDescription(), product.getStock(), product.getType());
  }

}
