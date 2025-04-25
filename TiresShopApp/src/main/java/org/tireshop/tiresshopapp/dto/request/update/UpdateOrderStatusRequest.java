package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.OrderStatus;

@Schema(description = "Aktualizacja statusu zamówienia")
public record UpdateOrderStatusRequest(
        @Schema(example = "SHIPPED") OrderStatus status
        ) {
}
