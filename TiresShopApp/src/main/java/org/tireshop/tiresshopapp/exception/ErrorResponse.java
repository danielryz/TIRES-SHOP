package org.tireshop.tiresshopapp.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "ErrorResponse", description = "Standard error format")
public class ErrorResponse {
  @Schema(example = "error message")
  private String message;
  @Schema(example = "status")
  private int status;
  @Schema(example = "2025-05-10T17:11:23")
  private LocalDateTime timestamp;

  public ErrorResponse(String message, int status) {
    this.message = message;
    this.status = status;
    this.timestamp = LocalDateTime.now();
  }

}
