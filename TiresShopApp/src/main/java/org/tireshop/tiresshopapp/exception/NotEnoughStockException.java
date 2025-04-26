package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotEnoughStockException extends RuntimeException {
  public NotEnoughStockException() {
    super("Not enough stock to add to cart.");
  }
}
