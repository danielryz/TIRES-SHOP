package org.tireshop.tiresshopapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddToCartRequest(
        @Schema(example = "1", description = "ID produktu") Long productId,
        @Schema(example = "2", description = "Ilość produktu") int quantity
) {
}
