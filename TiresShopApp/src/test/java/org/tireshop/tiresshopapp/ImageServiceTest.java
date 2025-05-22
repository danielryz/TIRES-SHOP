package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;
import org.tireshop.tiresshopapp.dto.request.create.AddImagesRequest;
import org.tireshop.tiresshopapp.dto.request.create.CreateImageRequest;
import org.tireshop.tiresshopapp.dto.response.ImageResponse;
import org.tireshop.tiresshopapp.entity.Image;
import org.tireshop.tiresshopapp.entity.Product;
import org.tireshop.tiresshopapp.exception.ImageNotFoundException;
import org.tireshop.tiresshopapp.repository.ImageRepository;
import org.tireshop.tiresshopapp.repository.ProductRepository;
import org.tireshop.tiresshopapp.service.CloudinaryService;
import org.tireshop.tiresshopapp.service.ImageService;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

  @Mock
  private ImageRepository imageRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CloudinaryService cloudinaryService;

  @InjectMocks
  private ImageService imageService;

  private Product product;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    product = new Product();
    product.setId(1L);
  }

  @Test
  void shouldReturnAllImages() {
    Image image = new Image(1L, "url", product, "publicId");
    when(imageRepository.findAll()).thenReturn(List.of(image));

    List<ImageResponse> responses = imageService.getAllImages();

    assertEquals(1, responses.size());
    assertEquals("url", responses.get(0).url());
  }

  @Test
  void shouldReturnImageById_WhenExists() {
    Image image = new Image(1L, "url", product, "publicId");
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

    ImageResponse response = imageService.getImageById(1L);

    assertEquals("url", response.url());
  }

  @Test
    void shouldThrowException_WhenImageNotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> imageService.getImageById(1L));
    }

  @Test
  void shouldReturnImagesByProductId() {
    Image image = new Image(1L, "url", product, "publicId");
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(imageRepository.findByProduct(product)).thenReturn(List.of(image));

    List<ImageResponse> responses = imageService.getImagesByProductId(1L);

    assertEquals(1, responses.size());
    assertEquals("url", responses.get(0).url());
  }

  @Test
  void shouldCreateImage() {
    CreateImageRequest request = new CreateImageRequest("url", 1L, "publicId");
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    imageService.createImage(request);

    verify(imageRepository, times(1)).save(any(Image.class));
  }

  @Test
  void shouldAddImagesToProduct() {
    AddImagesRequest request = new AddImagesRequest("url", "publicId");
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    imageService.addImagesToProduct(1L, List.of(request));

    verify(imageRepository, times(1)).save(any(Image.class));
  }

  @Test
    void shouldDeleteImagesByProductId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        imageService.deleteImagesByProductId(1L);

        verify(imageRepository, times(1)).deleteByProduct(product);
    }

  @Test
  void shouldSaveImageToCloudinaryAndDatabase() throws IOException {
    MultipartFile file = mock(MultipartFile.class);
    Map<String, String> uploadResult = Map.of("url", "image-url", "public_id", "publicId");
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(cloudinaryService.uploadFile(file)).thenReturn(uploadResult);
    when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> {
      Image saved = invocation.getArgument(0);
      saved.setId(99L);
      return saved;
    });

    ImageResponse response = imageService.saveImage(file, 1L);

    assertEquals("image-url", response.url());
    assertEquals("publicId", response.publicId());
  }

  @Test
  void shouldDeleteImage_WhenExistsAndCloudinaryDeletesSuccessfully() {
    Image image = new Image(1L, "url", product, "publicId");
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
    when(cloudinaryService.deleteFile("publicId")).thenReturn(true);

    imageService.deleteImage(1L);

    verify(imageRepository, times(1)).deleteById(1L);
  }

  @Test
  void shouldThrowException_WhenCloudinaryFailsToDelete() {
    Image image = new Image(1L, "url", product, "publicId");
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
    when(cloudinaryService.deleteFile("publicId")).thenReturn(false);

    assertThrows(RuntimeException.class, () -> imageService.deleteImage(1L));
  }
}
