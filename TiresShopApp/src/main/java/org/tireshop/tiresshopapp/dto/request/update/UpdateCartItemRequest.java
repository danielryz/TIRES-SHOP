package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateCartItemRequest(
        @Schema(example = "5", description = "Nowa ilość") int quantity
) {
}
