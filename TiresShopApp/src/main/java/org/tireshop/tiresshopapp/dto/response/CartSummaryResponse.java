package org.tireshop.tiresshopapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public record CartSummaryResponse(
        @Schema(description = "Lista pozycji w koszyku") List<CartItemResponse> items,
        @Schema(description = "Łączna wartość koszyka", example = "899.99") BigDecimal total
) {}
