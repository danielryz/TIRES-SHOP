package org.tireshop.tiresshopapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c3VyQHRlc3QuY29tIiwiaWF0IjoxNjk4NzAzNzAwLCJleHAiOjE2OTg3MDczMDB9._S2Z1WlvVO2gN5e5m7JknC3aMkGgCB5Bt-8oEbvAeLw")
        String token
) {
}
