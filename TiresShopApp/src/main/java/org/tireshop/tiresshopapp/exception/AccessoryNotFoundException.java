package org.tireshop.tiresshopapp.exception;

import org.tireshop.tiresshopapp.entity.AccessoryType;

public class AccessoryNotFoundException extends RuntimeException {
  public AccessoryNotFoundException(Long id) {
    super("Accessory with id " + id + " not found.");
  }

  public AccessoryNotFoundException(AccessoryType type) {
    super("Accessory with filter " + type + " not found.");
  }
}
