package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
public record UpdateTireRequest(
        UpdateProductRequest request,
        @Schema(example = "WINTER") String season,
        @Schema(example = "205/55R16") String size
) {
}
