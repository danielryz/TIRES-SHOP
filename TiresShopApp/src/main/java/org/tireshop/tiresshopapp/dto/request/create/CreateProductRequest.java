package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public record CreateProductRequest(
        @Schema(example = "Inne") String name,
        @Schema(example = "299.99") BigDecimal price,
        @Schema(example = "Opis") String description,
        @Schema(example = "15") int stock,
        @Schema(example = "ALL") ProductType type
) {
}
