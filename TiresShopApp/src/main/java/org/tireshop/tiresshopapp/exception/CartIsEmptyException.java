package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartIsEmptyException extends RuntimeException {
  public CartIsEmptyException() {
    super();
  }
}
