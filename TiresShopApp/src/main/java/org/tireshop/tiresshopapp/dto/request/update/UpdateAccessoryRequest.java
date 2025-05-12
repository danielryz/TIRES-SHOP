package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.AccessoryType;

public record UpdateAccessoryRequest(

        UpdateProductRequest request,
        @Schema(example = "ALUMINIUM")
        AccessoryType accessoryType
) {
}
