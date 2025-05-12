package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateProductRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateProductRequest;
import org.tireshop.tiresshopapp.dto.response.ProductResponse;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.exception.ProductNotFoundException;
import org.tireshop.tiresshopapp.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  // GET
  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream().map(this::mapToResponse).toList();
  }

  public ProductResponse getProductById(Long id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    return mapToResponse(product);
  }

  // POST
  @Transactional
  public ProductResponse createProduct(CreateProductRequest request) {
    Product product = new Product();
    product.setName(request.name());
    product.setPrice(request.price());
    product.setDescription(request.description());
    product.setStock(request.stock());
    product.setType(request.type());

    return mapToResponse(productRepository.save(product));
  }

  // PATCH
  @Transactional
  public void updateProduct(Long id, UpdateProductRequest request) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    if(request.name() != null && !request.name().isBlank())
      product.setName(request.name());
    if(request.price() != null)
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
    productRepository.deleteById(id);
  }

  private ProductResponse mapToResponse(Product product) {
    return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
        product.getDescription(), product.getStock(), product.getType());
  }

}
