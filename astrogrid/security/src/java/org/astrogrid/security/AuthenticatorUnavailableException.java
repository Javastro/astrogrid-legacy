package org.astrogrid.security;

/**
 * An exception thrown when an authentication mechanism,
 * e.g. AstroGrid community service fails to run an
 * identity check.
 *
 * @author Guy Rixon
 */
public class AuthenticatorUnavailableException extends SecurityException {

  public AuthenticatorUnavailableException (String message) {
    super(message);
  }

  public AuthenticatorUnavailableException (String message, Throwable t) {
    super(message, t);
  }

}