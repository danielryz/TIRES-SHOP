package org.tireshop.tiresshopapp.exception;

public class OrderIsPaidException extends RuntimeException {
  public OrderIsPaidException() {
    super("Order already paid");
  }
}
