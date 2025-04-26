package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateImageRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateImageRequest;
import org.tireshop.tiresshopapp.dto.response.ImageResponse;
import org.tireshop.tiresshopapp.entity.Image;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.exception.ImageNotFoundException;
import org.tireshop.tiresshopapp.exception.ProductNotFoundException;
import org.tireshop.tiresshopapp.repository.ImageRepository;
import org.tireshop.tiresshopapp.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageRepository imageRepository;
  private final ProductRepository productRepository;

  public List<ImageResponse> getAllImages() {
    return imageRepository.findAll().stream().map(this::mapToResponse).toList();
  }

  public ImageResponse getImageById(Long id) {
    Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(id));
    return mapToResponse(image);
  }

  public List<ImageResponse> getImagesByProductId(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));

    return imageRepository.findByProduct(product).stream().map(this::mapToResponse).toList();
  }

  @Transactional
  public void createImage(CreateImageRequest request) {
    Product product = productRepository.findById(request.productId())
        .orElseThrow(() -> new ProductNotFoundException(request.productId()));

    Image image = new Image();
    image.setUrl(request.url());
    image.setProduct(product);

    imageRepository.save(image);

  }

  @Transactional
  public void updateImage(Long id, UpdateImageRequest request) {
    Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(id));

    if (request.url() != null && !request.url().isBlank()) {
      image.setUrl(request.url());
    }
    imageRepository.save(image);
  }

  @Transactional
  public void deleteImage(Long id) {
    if (!imageRepository.existsById(id)) {
      throw new ImageNotFoundException(id);
    }
    imageRepository.deleteById(id);
  }

  @Transactional
  public void deleteImagesByProductId(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));

    imageRepository.deleteByProduct(product);
  }

  private ImageResponse mapToResponse(Image image) {
    return new ImageResponse(image.getId(), image.getUrl(), image.getProduct().getId());
  }
}
