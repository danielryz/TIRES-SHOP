package org.tireshop.tiresshopapp.exception;

import org.tireshop.tiresshopapp.entity.User;

public class NoOrderActiveForUserException extends RuntimeException {
  public NoOrderActiveForUserException(User user) {
    super("No active order found for user " + user);
  }

  public NoOrderActiveForUserException(String sessionId) {
    super("No active order found for session id: " + sessionId);
  }
}
