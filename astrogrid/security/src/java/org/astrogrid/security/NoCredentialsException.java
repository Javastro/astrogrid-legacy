package org.astrogrid.security;

/**
 * Exception to be thrown when no credentials are found.
 * This could be used in a handler looking for credentials
 * in message, in code attempting authentication, or in
 * code trying to send an authenticated message.  The
 * message in the exception should make clear the context.
 *
 * @author Guy Rixon
 */
public class NoCredentialsException extends SecurityException {

  public NoCredentialsException (String message) {
    super(message);
  }

  public NoCredentialsException (String message, Throwable t) {
    super(message, t);
  }

}