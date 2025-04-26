package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends ResponseStatusException {
  public UserNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "User with id " + id + " not found.");
  }

  public UserNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Logged in user not found.");
  }
}
