package org.tireshop.tiresshopapp.exception;

public class UserAlreadyExistException extends RuntimeException {
  public static UserAlreadyExistException byEmail(String email) {
    return new UserAlreadyExistException("User with email " + email + " already exists");
  }

  public static UserAlreadyExistException byUsername(String username) {
    return new UserAlreadyExistException("User with username " + username + " already exist");
  }

  private UserAlreadyExistException(String message) {
    super(message);
  }

}
