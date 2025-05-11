package org.tireshop.tiresshopapp.exception;

public class CartIsEmptyException extends RuntimeException {
  public CartIsEmptyException() {
    super("Cart is empty");
  }
}
