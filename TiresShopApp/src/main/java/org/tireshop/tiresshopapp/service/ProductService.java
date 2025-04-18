package org.tireshop.tiresshopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.CreateProductRequest;
import org.tireshop.tiresshopapp.dto.request.UpdateProductRequest;
import org.tireshop.tiresshopapp.dto.response.ProductResponse;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id).map(this::mapToResponse);
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setDescription(request.description());
        product.setStock(request.stock());
        product.setType(request.type());

        return mapToResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produkt o id " + id + " nie znaleziony"));

        if(request.name() != null && request.name().isBlank()) product.setName(request.name());
        if(request.price() != null) product.setPrice(request.price());
        if(request.description() != null && request.description().isBlank()) product.setDescription(request.description());
        if(request.stock() != null) product.setStock(request.stock());
        if(request.type() != null) product.setType(request.type());

        return mapToResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)){
            throw new RuntimeException("Produkt o id " + id + " nie znaleziony");
        }
        productRepository.deleteById(id);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                product.getType()
        );
    }

}
