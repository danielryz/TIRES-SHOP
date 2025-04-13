package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public record CreateRimRequest(
        @Schema(example = "Felga aluminiowa 17\" 5x112")
        String name,
        @Schema(example = "420,00")
        BigDecimal price,
        @Schema(example = "Nowoczesna felga do aut sportowych")
        String description,
        @Schema(example = "6")
        int stock,
        @Schema(example = "RIM")
        ProductType type,
        @Schema(example = "ALUMINIUM")
        String material,
        @Schema(example = "16")
        String size,
        @Schema(example = "5x112")
        String boltPattern
) {
}
