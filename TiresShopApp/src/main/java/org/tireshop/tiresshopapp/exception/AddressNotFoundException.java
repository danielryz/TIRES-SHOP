package org.tireshop.tiresshopapp.exception;

public class AddressNotFoundException extends RuntimeException {
  public AddressNotFoundException() {
    super("Address not found");
  }
}
