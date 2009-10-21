package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * An exception that should be signalled to the client by a TAP
 * error-document. This type should be used to wrap exceptions that must be
 * reported using the TAP mechanism. It can also be used directly, without
 * a wrapped exception.
 *
 * @author Guy Rixon
 */
public class TapException extends Exception {
  /**
   * Constructs an exception with a given message.
   *
   * @param message The error message.
   */
  public TapException(String message) {
    super(message);
  }

  /**
   * Constructs an exception wrapping a given exception.
   * When the error document is produced, the error message is taken
   * from the wrapped exception.
   *
   * @param cause The wrapped exception.
   */
  public TapException (Throwable cause) {
    super(cause);
  }

  /**
   * Commits an error document expressing the exception.
   *
   * @param response The HTTP response to which the document is sent.
   * @throws IOException If the document cannot be committed.
   */
  public void writeTapErrorDocument(HttpServletResponse response) throws IOException {

    // Find an error message to put in the report.
    String message = null;
    Throwable cause = getCause();
    if (cause == null) {
      message = getMessage();
    }
    else {
      message = cause.getMessage();
    }

    // KLUDGE: send a 500 with HTML error message.
    // TODO write a proper TAP document.
    if (message == null) {
      response.sendError(500);
    }
    else {
      response.sendError(500, message);
    }
  }

}
