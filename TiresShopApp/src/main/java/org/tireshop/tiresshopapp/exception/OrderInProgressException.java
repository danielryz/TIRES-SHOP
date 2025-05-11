package org.tireshop.tiresshopapp.exception;

public class OrderInProgressException extends RuntimeException {
  public OrderInProgressException() {
    super("You cannot cancel an order that is in progress.");
  }
}
