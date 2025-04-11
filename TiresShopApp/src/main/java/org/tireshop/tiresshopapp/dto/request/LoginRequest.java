package org.tireshop.tiresshopapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(example = "tires@tiresshop.pl")
        String email,
        @Schema(example = "Pa$$word1")
        String password
) {
}
