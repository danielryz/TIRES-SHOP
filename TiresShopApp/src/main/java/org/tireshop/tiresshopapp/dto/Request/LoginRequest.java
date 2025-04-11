package org.tireshop.tiresshopapp.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    @Schema(example = "tires@tiresshop.pl")
    private String email;
    @Schema(example = "Pa$$word1")
    private String password;
}
