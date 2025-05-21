package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dane do dodania zdjęcia do produktu")
public record CreateImageRequest(
        @Schema(example = "https://link.com", description = "Adres Url") String url,
        @Schema(example = "1", description = "Id produktu") Long productId,
        @Schema(example = "/upload") String publicId
) {
}
