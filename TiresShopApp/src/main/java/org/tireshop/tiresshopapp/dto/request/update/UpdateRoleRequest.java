package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateRoleRequest(
        @Schema(example = "ROLE_MANAGER")
        String name
) {
}