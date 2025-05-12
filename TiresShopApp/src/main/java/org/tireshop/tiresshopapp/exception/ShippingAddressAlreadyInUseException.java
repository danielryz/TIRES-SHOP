package org.tireshop.tiresshopapp.exception;

public class ShippingAddressAlreadyInUseException extends RuntimeException {
  public ShippingAddressAlreadyInUseException() {
    super("Shipping address already in use in order");
  }
}
