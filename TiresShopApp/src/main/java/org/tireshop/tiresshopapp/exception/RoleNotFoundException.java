package org.tireshop.tiresshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends ResponseStatusException {
  public RoleNotFoundException(String roleName) {
    super(HttpStatus.NOT_FOUND, roleName + " not found.");
  }

  public RoleNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "Role with id " + id + " not found.");
  }
}
