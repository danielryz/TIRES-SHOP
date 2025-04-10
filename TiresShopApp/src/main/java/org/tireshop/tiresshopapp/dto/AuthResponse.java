package org.tireshop.tiresshopapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c3VyQHRlc3QuY29tIiwiaWF0IjoxNjk4NzAzNzAwLCJleHAiOjE2OTg3MDczMDB9._S2Z1WlvVO2gN5e5m7JknC3aMkGgCB5Bt-8oEbvAeLw")
    private String token;
}
