package org.tireshop.tiresshopapp.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

  private final Cloudinary cloudinary;

  public Map<String, String> uploadFile(MultipartFile file) throws IOException {
    @SuppressWarnings("unchecked")
    Map<String, Object> uploadResult =
        (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), Map.of());

    return Map.of("url", uploadResult.get("secure_url").toString(), "public_id",
        uploadResult.get("public_id").toString());
  }

  public boolean deleteFile(String publicId) {
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> result =
          (Map<String, Object>) cloudinary.uploader().destroy(publicId, Map.of());

      return "ok".equals(result.get("result"));
    } catch (Exception e) {
      log.error("Error Deleting file from Cloudinary: {}", publicId, e);
      return false;
    }
  }
}
