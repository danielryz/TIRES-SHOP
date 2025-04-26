package org.tireshop.tiresshopapp.exception;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  @Operation(hidden = true)
  public Map<String, String> handleBadCredentials(BadCredentialsException ex) {
    return Map.of("error", "Invalid email or password.");
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  @Operation(hidden = true)
  public Map<String, String> handleAccessDenied(AccessDeniedException ex) {
    return Map.of("error", ex.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseBody
  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleRuntime(RuntimeException ex) {
    return Map.of("error", ex.getMessage());
  }

  @ExceptionHandler(CartIsEmptyException.class)
  @ResponseBody
  @Operation(hidden = true)
  public Map<String, String> handleNotFoundCart() {
    return Map.of("error", "Cart is empty. You cannot place an order.");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  @Operation(hidden = true)
  public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    return ResponseEntity.badRequest().body(Map.of("error", Objects.requireNonNull(message)));
  }

}


