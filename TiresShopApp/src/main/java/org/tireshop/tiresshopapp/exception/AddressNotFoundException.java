package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends ResponseStatusException {
  public AddressNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Address not found");
  }
}
