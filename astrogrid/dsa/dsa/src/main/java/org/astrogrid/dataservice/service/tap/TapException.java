package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.io.PrintWriter;
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

    String code = ("OK".equals(message))? "OK" : "ERROR";

    PrintWriter pw = response.getWriter();
    pw.println("<?xml version='1.0'?>");
    pw.println("<VOTABLE xmlns='http://www.ivoa.net/xml/VOTable/v1.2' " +
               "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
               "xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.2 " +
               "http://www.ivoa.net/xml/VOTable/v1.2' " +
               "version='1.2'>");
    pw.println("  <RESOURCE type='results'>");
    pw.println("    <INFO name='QUERY_STATUS' value='" + code + "'>" + message + "</INFO>");
    pw.println("  </RESOURCE>");
    pw.println("</VOTABLE>");
    pw.close();
  }

}
