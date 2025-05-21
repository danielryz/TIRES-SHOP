package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public record UpdateTireRequest(
        @Schema(example = "Opona zimowa 205/55R16") String name,
        @Schema(example = "299.99") BigDecimal price,
        @Schema(example = "Doskonała przyczepność w zimowych warunkach") String description,
        @Schema(example = "15") Integer stock,
        @Schema(example = "TIRE") ProductType type,
        @Schema(example = "WINTER") String season,
        @Schema(example = "205/55R16") String size
) {
}
