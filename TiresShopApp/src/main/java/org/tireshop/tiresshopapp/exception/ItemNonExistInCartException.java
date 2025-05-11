package org.tireshop.tiresshopapp.exception;

public class ItemNonExistInCartException extends RuntimeException {
  public ItemNonExistInCartException() {
    super("The item in the cart does not exist.");
  }
}
