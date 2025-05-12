package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public record UpdateRimRequest(
        UpdateProductRequest request,
        @Schema(example = "ALUMINIUM")
        String material,
        @Schema(example = "16")
        String size,
        @Schema(example = "5x112")
        String boltPattern
) {
}
