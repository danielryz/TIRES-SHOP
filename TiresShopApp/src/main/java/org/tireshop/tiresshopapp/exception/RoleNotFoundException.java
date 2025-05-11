package org.tireshop.tiresshopapp.exception;

public class RoleNotFoundException extends RuntimeException {
  public RoleNotFoundException(String roleName) {
    super("Role " + roleName + " not found.");
  }

  public RoleNotFoundException(Long id) {
    super("Role with id " + id + " not found.");
  }
}
