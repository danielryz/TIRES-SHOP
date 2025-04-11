package org.tireshop.tiresshopapp.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateUserRolesRequest {
    @Schema(
            example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]",
            description = "Lista ról do przypisania użytkownikowi"
    )
    private Set<String> roles;
}
