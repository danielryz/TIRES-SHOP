package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RimNotFoundException extends ResponseStatusException {
  public RimNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "Rim with id " + id + " not found.");
  }

  public RimNotFoundException(String name) {
    super(HttpStatus.NOT_FOUND, "Rim with filter " + name + " not found.");
  }
}
