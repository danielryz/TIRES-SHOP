package org.tireshop.tiresshopapp.exception;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
    ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    ErrorResponse response =
        new ErrorResponse(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials() {
    ErrorResponse response =
        new ErrorResponse("Invalid email or password.", HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorResponse> handleInvalidPassword(InvalidPasswordException ex) {
    ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(TireNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTireNotFound(TireNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(RimNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRimNotFound(RimNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(AccessoryNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAccessoryNotFound(AccessoryNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(AddressNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAddressNotFound(AddressNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ImageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleImageNotFound(ImageNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ItemNonExistInCartException.class)
  public ResponseEntity<ErrorResponse> handleItemNonExistInCart(ItemNonExistInCartException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(NotEnoughStockException.class)
  public ResponseEntity<ErrorResponse> handleNotEnoughStock(NotEnoughStockException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(CartIsEmptyException.class)
  public ResponseEntity<ErrorResponse> handleCartIsEmpty(CartIsEmptyException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExist(UserAlreadyExistException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(OrderInProgressException.class)
  public ResponseEntity<ErrorResponse> handleOrderInProgress(OrderInProgressException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoOrderActiveForUserException.class)
  public ResponseEntity<ErrorResponse> handleNoOrderActiveForUser(
      NoOrderActiveForUserException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoShippingAddressAssignException.class)
  public ResponseEntity<ErrorResponse> handleNoShippingAddressAssign(
      NoShippingAddressAssignException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(ShippingAddressAlreadyInUseException.class)
  public ResponseEntity<ErrorResponse> handleShippingAddressAlreadyInUse(
      ShippingAddressAlreadyInUseException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @Operation(hidden = true)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneral() {
    ErrorResponse error =
        new ErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}


