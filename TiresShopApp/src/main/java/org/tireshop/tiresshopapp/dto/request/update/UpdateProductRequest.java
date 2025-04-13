package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public record UpdateProductRequest(

        @Schema(example = "Opona całoroczna 205/55R16") String name,
        @Min(0)
        @Schema(example = "349.99") BigDecimal price,
        @Schema(example = "Znakomita w każdej porze roku") String description,
        @Min(0)
        @Schema(example = "10") Integer stock,
        @Schema(example = "TIRE") ProductType type
) {
}
