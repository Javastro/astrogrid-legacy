package org.astrogrid.security;

/**
 * An exception thrown when an account name is rejected by an
 * authenticating agent.
 *
 * @author Guy Rixon
 */
public class InvalidAccountException extends SecurityException {

  public InvalidAccountException (String message) {
    super(message);
  }

  public InvalidAccountException (String message, Throwable t) {
    super(message, t);
  }

}