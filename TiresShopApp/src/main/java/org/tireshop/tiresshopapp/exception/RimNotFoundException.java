package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RimNotFoundException extends ResponseStatusException {
  public RimNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "Rim with id " + id + " not found.");
  }

  public RimNotFoundException(String name) {
    super(HttpStatus.NOT_FOUND, "Rim with filter " + name + " not found.");
  }
}
