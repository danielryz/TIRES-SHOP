package org.tireshop.tiresshopapp.exception;

public class NoShippingAddressAssignException extends RuntimeException {
  public NoShippingAddressAssignException() {
    super("No shipping address assigned to this order.");
  }
}
