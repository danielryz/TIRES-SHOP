package org.tireshop.tiresshopapp.exception;

public class UsernameAlreadyExistException extends RuntimeException {
  public UsernameAlreadyExistException() {
    super("Username already exist");
  }
}
