package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends ResponseStatusException {
  public OrderNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "Order with id " + id + " not found.");
  }
}
