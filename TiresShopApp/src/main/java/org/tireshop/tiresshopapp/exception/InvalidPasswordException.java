package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException() {
    super("Invalid current password.");
  }
}
