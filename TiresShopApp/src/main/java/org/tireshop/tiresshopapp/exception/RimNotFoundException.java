package org.tireshop.tiresshopapp.exception;

public class RimNotFoundException extends RuntimeException {
  public RimNotFoundException(Long id) {
    super("Rim with id " + id + " not found.");
  }

  public RimNotFoundException(String name) {
    super("Rim with filter " + name + " not found.");
  }
}
