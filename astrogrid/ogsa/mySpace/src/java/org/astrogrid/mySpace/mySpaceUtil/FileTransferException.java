package org.astrogrid.mySpace.mySpaceUtil;

/**
 * An exception thrown by the {@link FileTransfer} class.
 * This kind of exception behaves exactly as the normal
 * java.lang.Exception in the Java-1.4 form.  The only
 * difference in this subclass is the name, which enables
 * the exceptions to be caught separately.
 *
 * @author Jia Yu
 * @author Guy Rixon
 */
public class FileTransferException extends Exception {

  /**
   * Constructs an exception with no message and no cause.
   */
  public FileTransferException () {};

  /**
   * Constructs an exception with a message but no cause.
   *
   * @param message the meaning of the exception.
   */
  public FileTransferException (final String message) {
    super(message);
  }

  /**
   * Constructs an exception with a message and a cause.
   *
   * @param message the meaning of the exception.
   * @param cause   the exception that caused the current exception
   *                to be thrown.
   */
  public FileTransferException (final String message, final Throwable cause) {
    super(message, cause);
  }

}
