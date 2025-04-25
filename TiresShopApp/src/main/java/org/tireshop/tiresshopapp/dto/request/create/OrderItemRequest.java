package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Produkt i ilość do zamówienia")
public record OrderItemRequest(
        @Schema(example = "4") Long productId,
        @Schema(example = "3") int quantity
) {
}
