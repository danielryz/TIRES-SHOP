package org.tireshop.tiresshopapp.exception;

public class TireNotFoundException extends RuntimeException {
  public TireNotFoundException(Long id) {
    super("Tire with id " + id + " not found.");
  }

  public TireNotFoundException(String name) {
    super("Tire with filter " + name + " not found.");
  }
}
