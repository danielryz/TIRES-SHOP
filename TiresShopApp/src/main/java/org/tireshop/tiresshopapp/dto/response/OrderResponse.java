package org.tireshop.tiresshopapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Schema(description = "Zamówienie")
public record OrderResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "CREATED") OrderStatus status,
        @Schema(example = "899.97") BigDecimal totalAmount,
        @Schema(description = "Lista prodóktów w zamówieniu") List<OrderItemResponse> items,
        @Schema(example = "2025-04-10T15:30:00")LocalDateTime createdAt,
        @Schema(example = "jan.kowalski@example.com") String questEmail,
        @Schema(example = "Jan") String questFirstName,
        @Schema(example = "Kowalski") String questLastName,
        @Schema(example = "t") Boolean isPaid,
        @Schema(example = "2025-04-10T15:30:00") LocalDateTime paidAt
        ) {

}
