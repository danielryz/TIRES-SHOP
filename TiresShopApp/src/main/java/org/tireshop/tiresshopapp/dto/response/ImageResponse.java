package org.tireshop.tiresshopapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Dane zdjęcia")
public record ImageResponse(
        @Schema(example = "1", description = "Id zdjęcia") Long id,
        @Schema(example = "https://link.com", description = "Adres Url") String url,
        @Schema(example = "1", description = "Id produktu") Long productId
) {
}
