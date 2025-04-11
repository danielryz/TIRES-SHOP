package org.tireshop.tiresshopapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public record ProductResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Opona zimowa 205/55R16") String name,
        @Schema(example = "299.99") BigDecimal price,
        @Schema(example = "Doskonała przyczepność w zimowych warunkach") String description,
        @Schema(example = "15") int stock,
        @Schema(example = "TIRE") ProductType type
) {
}
