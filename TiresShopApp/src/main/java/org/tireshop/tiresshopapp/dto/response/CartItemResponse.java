package org.tireshop.tiresshopapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CartItemResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long productId,
        @Schema(example = "Opona zimowa 205/55R16") String productName,
        @Schema(example = "2") int quantity,
        @Schema(example = "299.99") BigDecimal pricePerItem,
        @Schema(example = "599.98") BigDecimal totalPrice
) {
}
