package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dane do stworzenia zamówienia")
public record CreateOrderRequest(
        @Schema(example = "Jan") String guestFirstName,
        @Schema(example = "Kowalski") String guestLastName,
        @Schema(example = "jan.kowalski@example.com") String guestEmail,
        @Schema(example = "+40 123 456 789") String guestPhoneNumber,
        @Schema(description = "Lista produktów do zamówienia")List<OrderItemRequest> items
        ) {
}
