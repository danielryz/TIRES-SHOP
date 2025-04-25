package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dane do zaktualizowania zdjęcia do produktu")
public record UpdateImageRequest(
        @Schema(example = "https://link.com", description = "Adres Url") String url
) {
}
