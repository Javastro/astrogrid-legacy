package org.astrogrid.common.creator;


/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class InstanceCreatorException extends Exception {
  /**
   * @param message
   */
  public InstanceCreatorException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public InstanceCreatorException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public InstanceCreatorException(Throwable cause) {
    super(cause);
  }

}