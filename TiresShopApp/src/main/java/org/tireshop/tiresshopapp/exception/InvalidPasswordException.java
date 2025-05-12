package org.tireshop.tiresshopapp.exception;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException() {
    super("Invalid current password.");
  }
}
