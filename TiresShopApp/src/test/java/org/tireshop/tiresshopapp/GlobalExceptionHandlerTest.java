package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.tireshop.tiresshopapp.entity.AccessoryType;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.*;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void shouldHandleUserNotFoundException() {
    Long userId = 1L;
    String message = "User with id " + userId + " not found.";
    UserNotFoundException ex = new UserNotFoundException(userId);

    ResponseEntity<ErrorResponse> response = handler.handleUserNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleUserAlreadyExistsException() {
    String email = "<EMAIL>";
    String message = "User with email " + email + " already exists";
    UserAlreadyExistException ex = UserAlreadyExistException.byEmail(email);

    ResponseEntity<ErrorResponse> response = handler.handleUserAlreadyExist(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(409, response.getBody().getStatus());
  }

  @Test
  void shouldHandleTireNotFoundExceptionId() {
    Long tireId = 1L;
    String message = "Tire with id " + tireId + " not found.";
    TireNotFoundException ex = new TireNotFoundException(tireId);

    ResponseEntity<ErrorResponse> response = handler.handleTireNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleTireNotFoundExceptionName() {
    String name = "Tire name";
    String message = "Tire with filter " + name + " not found.";
    TireNotFoundException ex = new TireNotFoundException(name);

    ResponseEntity<ErrorResponse> response = handler.handleTireNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleRoleNotFoundException() {
    Long roleId = 1L;
    String message = "Role with id " + roleId + " not found.";
    RoleNotFoundException ex = new RoleNotFoundException(roleId);

    ResponseEntity<ErrorResponse> response = handler.handleRoleNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleRimNotFoundExceptionId() {
    Long rimId = 1L;
    String message = "Rim with id " + rimId + " not found.";
    RimNotFoundException ex = new RimNotFoundException(rimId);

    ResponseEntity<ErrorResponse> response = handler.handleRimNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleRimNotFoundExceptionName() {
    String name = "Rim name";
    String message = "Rim with filter " + name + " not found.";
    RimNotFoundException ex = new RimNotFoundException(name);

    ResponseEntity<ErrorResponse> response = handler.handleRimNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleAccessoryNotFoundExceptionId() {
    Long accessoryId = 1L;
    String message = "Accessory with id " + accessoryId + " not found.";
    AccessoryNotFoundException ex = new AccessoryNotFoundException(accessoryId);

    ResponseEntity<ErrorResponse> response = handler.handleAccessoryNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleAccessoryNotFoundExceptionName() {
    AccessoryType type = AccessoryType.BOLT;
    String message = "Accessory with filter " + type + " not found.";
    AccessoryNotFoundException ex = new AccessoryNotFoundException(type);

    ResponseEntity<ErrorResponse> response = handler.handleAccessoryNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleProductNotFoundException() {
    Long productId = 1L;
    String message = "Product with id " + productId + " not found";
    ProductNotFoundException ex = new ProductNotFoundException(productId);

    ResponseEntity<ErrorResponse> response = handler.handleProductNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleOrderNotFoundException() {
    Long orderId = 1L;
    String message = "Order with id " + orderId + " not found.";
    OrderNotFoundException ex = new OrderNotFoundException(orderId);

    ResponseEntity<ErrorResponse> response = handler.handleOrderNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleOrderIsPaidException() {
    String message = "Order already paid";
    OrderIsPaidException ex = new OrderIsPaidException();

    ResponseEntity<ErrorResponse> response = handler.handleOrderIsPaid(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(409, response.getBody().getStatus());
  }

  @Test
  void shouldHandleOrderInProgressException() {
    String message = "You cannot cancel an order that is in progress.";
    OrderInProgressException ex = new OrderInProgressException();

    ResponseEntity<ErrorResponse> response = handler.handleOrderInProgress(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(409, response.getBody().getStatus());
  }

  @Test
  void shouldHandleNotEnoughStockException() {
    String message = "Not enough stock to add to cart.";
    NotEnoughStockException ex = new NotEnoughStockException();

    ResponseEntity<ErrorResponse> response = handler.handleNotEnoughStock(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(409, response.getBody().getStatus());
  }

  @Test
  void shouldHandleNoOrderActiveForUserExceptionClientId() {
    String sessionId = "abc123";
    String message = "No active order found for session id: " + sessionId;
    NoOrderActiveForUserException ex = new NoOrderActiveForUserException(sessionId);
    ResponseEntity<ErrorResponse> response = handler.handleNoOrderActiveForUser(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleNoOrderActiveForUserExceptionUser() {
    User user = new User();
    user.setId(1L);
    user.setEmail("test@exception.pl");
    user.setPassword("Pa$$word1");
    user.setUsername("test");
    String message = "No active order found for user " + user;
    NoOrderActiveForUserException ex = new NoOrderActiveForUserException(user);

    ResponseEntity<ErrorResponse> response = handler.handleNoOrderActiveForUser(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleItemNonExistInCartException() {
    String message = "The item in the cart does not exist.";
    ItemNonExistInCartException ex = new ItemNonExistInCartException();
    ResponseEntity<ErrorResponse> response = handler.handleItemNonExistInCart(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleInvalidPasswordException() {
    String message = "Invalid current password.";
    InvalidPasswordException ex = new InvalidPasswordException();
    ResponseEntity<ErrorResponse> response = handler.handleInvalidPassword(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(400, response.getBody().getStatus());
  }

  @Test
  void shouldHandleImageNotFoundException() {
    Long imageId = 1L;
    String message = "Image with id " + imageId + " not found.";
    ImageNotFoundException ex = new ImageNotFoundException(imageId);
    ResponseEntity<ErrorResponse> response = handler.handleImageNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleCartIsEmptyException() {
    String message = "Cart is empty";
    CartIsEmptyException ex = new CartIsEmptyException();
    ResponseEntity<ErrorResponse> response = handler.handleCartIsEmpty(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(400, response.getBody().getStatus());
  }

  @Test
  void shouldHandleAddressNotFoundException() {
    String message = "Address not found";
    AddressNotFoundException ex = new AddressNotFoundException();
    ResponseEntity<ErrorResponse> response = handler.handleAddressNotFound(ex);

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(404, response.getBody().getStatus());
  }

  @Test
  void shouldHandleBadCredentialsException() {
    String message = "Invalid email or password.";

    ResponseEntity<ErrorResponse> response = handler.handleBadCredentials();

    assertNotNull(response.getBody());
    assertEquals(message, response.getBody().getMessage());
    assertEquals(400, response.getBody().getStatus());
  }
}
