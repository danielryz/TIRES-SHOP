package org.tireshop.tiresshopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.create.CreateImageRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateImageRequest;
import org.tireshop.tiresshopapp.dto.response.ImageResponse;
import org.tireshop.tiresshopapp.entity.Image;
import org.tireshop.tiresshopapp.entity.Product;
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
    Image image = imageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Zdjęcie nie istnieje"));
    return mapToResponse(image);
  }

  public List<ImageResponse> getImagesByProductId(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Produkt nie istnieje"));

    return imageRepository.findByProduct(product).stream().map(this::mapToResponse).toList();
  }

  public ImageResponse createImage(CreateImageRequest request) {
    Product product = productRepository.findById(request.productId())
        .orElseThrow(() -> new RuntimeException("Produkt nie istnieje"));

    Image image = new Image();
    image.setUrl(request.url());
    image.setProduct(product);

    Image savedImage = imageRepository.save(image);
    return mapToResponse(savedImage);
  }

  public ImageResponse updateImage(Long id, UpdateImageRequest request) {
    Image image = imageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Zdjęcie nie istnieje"));

    if (request.url() != null && !request.url().isBlank()) {
      image.setUrl(request.url());
    }
    Image updatedImage = imageRepository.save(image);
    return mapToResponse(updatedImage);
  }

  public void deleteImage(Long id) {
    if (!imageRepository.existsById(id)) {
      throw new RuntimeException("Zdjęcie nie istnieje");
    }
    imageRepository.deleteById(id);
  }

  public void deleteImagesByProductId(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Produkt nie istnieje"));

    imageRepository.deleteByProduct(product);
  }

  private ImageResponse mapToResponse(Image image) {
    return new ImageResponse(image.getId(), image.getUrl(), image.getProduct().getId());
  }
}
