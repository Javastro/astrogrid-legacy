package org.astrogrid.community.server.subprocess;

/**
 * An exception in the use of program run in a subprocess.
 * This exception indicates that the program ran but returned
 * errors. Failure to launch or communicate with the subprocess
 * should be reported as an IOException instead of a SubprocessException.
 *
 * @author Guy Rixon
 */
public class SubprocessException extends Exception {

  /**
   * Constructs an exception with a given message and no cause.
   *
   * @param message The message.
   */
  public SubprocessException(String message) {
    super(message);
  }

  /**
   * Constructs an exception with a given message and cause.
   *
   * @param message The message.
   * @param cause The event that caused the current exception.
   */
  public SubprocessException(String message, Throwable cause) {
    super(message, cause);
  }
}
