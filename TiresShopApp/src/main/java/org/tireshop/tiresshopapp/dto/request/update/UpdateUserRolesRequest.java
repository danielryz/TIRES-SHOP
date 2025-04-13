package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;


public record UpdateUserRolesRequest(
        @Schema(
                example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]",
                description = "Lista ról do przypisania użytkownikowi"
        )
        Set<String> roles
) {
}
