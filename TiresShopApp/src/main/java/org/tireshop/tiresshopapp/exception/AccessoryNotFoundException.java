package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccessoryNotFoundException extends ResponseStatusException {
  public AccessoryNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "Accessory with id " + id + " not found.");
  }

  public AccessoryNotFoundException(String name) {
    super(HttpStatus.NOT_FOUND, "Accessory with filter " + name + " not found.");
  }
}
