package org.astrogrid.portal.myspace.acting.framework;

/**
 * Exception thrown by MySpace handlers.
 * 
 * @author peter.shillan
 * @see java.lang.Exception
 */
public class MySpaceHandlerException extends Exception {

  /**
   * @see java.lang.Exception#Exception() 
   */
  public MySpaceHandlerException() {
    super();
  }

  /**
   * @see java.lang.Exception#Exception(java.lang.String) 
   * @param message error message
   */
  public MySpaceHandlerException(String message) {
    super(message);
  }

  /**
   * @see java.lang.Exception#Exception(java.lang.Throwable) 
   * @param cause root cause of exception
   */
  public MySpaceHandlerException(Throwable cause) {
    super(cause);
  }

  /**
   * @see java.lang.Exception#Exception(java.lang.String, java.lang.Throwable) 
   * @param message error message
   * @param cause root cause of exception
   */
  public MySpaceHandlerException(String message, Throwable cause) {
    super(message, cause);
  }
}
