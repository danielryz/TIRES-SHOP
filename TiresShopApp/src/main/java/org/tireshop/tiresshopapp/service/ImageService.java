package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tireshop.tiresshopapp.dto.request.create.AddImagesRequest;
import org.tireshop.tiresshopapp.dto.request.create.CreateImageRequest;
import org.tireshop.tiresshopapp.dto.response.ImageResponse;
import org.tireshop.tiresshopapp.entity.Image;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.exception.ImageNotFoundException;
import org.tireshop.tiresshopapp.exception.ProductNotFoundException;
import org.tireshop.tiresshopapp.repository.ImageRepository;
import org.tireshop.tiresshopapp.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageRepository imageRepository;
  private final ProductRepository productRepository;
  private final CloudinaryService cloudinaryService;

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
    image.setPublicId(request.publicId());
    imageRepository.save(image);
  }

  public void addImagesToProduct(Long productId, List<AddImagesRequest> requests) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    for (AddImagesRequest request : requests) {
      Image image = new Image();
      image.setUrl(request.url());
      image.setProduct(product);
      image.setPublicId(request.publicId());
      imageRepository.save(image);
    }
  }

  @Transactional
  public void deleteImagesByProductId(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));

    imageRepository.deleteByProduct(product);
  }

  @Transactional
  public ImageResponse saveImage(MultipartFile file, Long productId) throws IOException {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
    String url = uploadResult.get("url");
    String publicId = uploadResult.get("public_id");

    Image image = new Image();
    image.setUrl(url);
    image.setPublicId(publicId);
    image.setProduct(product);

    return mapToResponse(imageRepository.save(image));
  }

  @Transactional
  public void deleteImage(Long imageId) {
    Image image =
        imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException(imageId));

    boolean deletedFromCloud = cloudinaryService.deleteFile(image.getPublicId());

    if (!deletedFromCloud) {
      throw new RuntimeException("Failed to delete image from Cloudinary");
    }

    imageRepository.deleteById(imageId);
  }

  private ImageResponse mapToResponse(Image image) {
    return new ImageResponse(image.getId(), image.getUrl(), image.getProduct().getId(),
        image.getPublicId());
  }
}
