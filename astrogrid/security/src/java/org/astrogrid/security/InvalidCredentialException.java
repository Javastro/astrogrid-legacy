package org.astrogrid.security;

/**
 * An exception thrown when a check on a credential, e.g. a password, fails.
 * This application should not be thrown if the account name is also invalid
 * (throw {@link InvalidAccountException} instead) or if the check fails
 * because the authentication mechanism breaks (throw
 * {@link AuthenticatorUnavailableException instead}).
 *
 * @author Guy Rixon
 */
public class InvalidCredentialException extends SecurityException {

  public InvalidCredentialException (String message) {
    super(message);
  }

  public InvalidCredentialException (String message, Throwable t) {
    super(message, t);
  }

}