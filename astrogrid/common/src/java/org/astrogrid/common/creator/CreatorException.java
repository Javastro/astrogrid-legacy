package org.astrogrid.common.creator;


/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class CreatorException extends Exception {
  /**
   * @param message
   */
  public CreatorException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public CreatorException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public CreatorException(Throwable cause) {
    super(cause);
  }

}