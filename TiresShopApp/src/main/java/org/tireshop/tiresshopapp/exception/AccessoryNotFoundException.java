package org.tireshop.tiresshopapp.exception;

public class AccessoryNotFoundException extends RuntimeException {
  public AccessoryNotFoundException(Long id) {
    super("Accessory with id " + id + " not found.");
  }

  public AccessoryNotFoundException(String name) {
    super("Accessory with filter " + name + " not found.");
  }
}
