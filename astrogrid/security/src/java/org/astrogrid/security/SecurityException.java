package org.astrogrid.security;

/**
 * A general exception for security matters.
 *
 * @author Guy Rixon
 */
public class SecurityException extends Exception {

  public SecurityException (String message) {
    super(message);
  }

  public SecurityException (String message, Throwable t) {
    super(message, t);
  }

}