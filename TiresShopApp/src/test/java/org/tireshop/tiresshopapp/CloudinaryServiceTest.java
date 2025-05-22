package org.tireshop.tiresshopapp;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import org.tireshop.tiresshopapp.service.CloudinaryService;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CloudinaryServiceTest {

  @Mock
  private Cloudinary cloudinary;

  @Mock
  private Uploader uploader;

  @Mock
  private MultipartFile multipartFile;

  @InjectMocks
  private CloudinaryService cloudinaryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldUploadFileSuccessfully() throws IOException {
    // Arrange
    byte[] fileBytes = "test content".getBytes();
    Map<String, Object> uploadResult = Map.of("secure_url", "https://cloudinary.com/test-image.jpg",
        "public_id", "test-public-id");

    when(multipartFile.getBytes()).thenReturn(fileBytes);
    when(cloudinary.uploader()).thenReturn(uploader);
    when(uploader.upload(fileBytes, Map.of())).thenReturn(uploadResult);

    // Act
    Map<String, String> result = cloudinaryService.uploadFile(multipartFile);

    // Assert
    assertEquals("https://cloudinary.com/test-image.jpg", result.get("url"));
    assertEquals("test-public-id", result.get("public_id"));
  }

  @Test
    void shouldThrowIOException_WhenUploadFails() throws IOException {
        // Arrange
        when(multipartFile.getBytes()).thenThrow(new IOException("Upload failed"));

        // Act & Assert
        assertThrows(IOException.class, () -> cloudinaryService.uploadFile(multipartFile));
    }

  @Test
  void shouldDeleteFileSuccessfully() throws Exception {
    // Arrange
    String publicId = "test-id";
    Map<String, Object> destroyResult = Map.of("result", "ok");

    when(cloudinary.uploader()).thenReturn(uploader);
    when(uploader.destroy(publicId, Map.of())).thenReturn(destroyResult);

    // Act
    boolean result = cloudinaryService.deleteFile(publicId);

    // Assert
    assertTrue(result);
  }

  @Test
  void shouldReturnFalse_WhenDeleteFails() throws Exception {
    // Arrange
    String publicId = "invalid-id";

    when(cloudinary.uploader()).thenReturn(uploader);
    when(uploader.destroy(publicId, Map.of())).thenThrow(new RuntimeException("Cloudinary error"));

    // Act
    boolean result = cloudinaryService.deleteFile(publicId);

    // Assert
    assertFalse(result);
  }
}
