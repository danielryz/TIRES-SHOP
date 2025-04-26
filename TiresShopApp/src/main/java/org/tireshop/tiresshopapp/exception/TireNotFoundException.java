package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TireNotFoundException extends ResponseStatusException {
  public TireNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "Tire with id " + id + " not found.");
  }

  public TireNotFoundException(String name) {
    super(HttpStatus.NOT_FOUND, "Tire with filter " + name + " not found.");
  }
}
