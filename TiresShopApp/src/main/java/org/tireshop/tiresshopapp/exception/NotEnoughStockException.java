package org.tireshop.tiresshopapp.exception;

public class NotEnoughStockException extends RuntimeException {
  public NotEnoughStockException() {
    super("Not enough stock to add to cart.");
  }
}
