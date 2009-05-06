package org.astrogrid.community.server.sso;

import org.mortbay.jetty.HttpHeaders;
import org.mortbay.jetty.testing.HttpTester;

/**
 * A specialization of org.mortbay.jetty.testing.HttpTester that supports
 * binary content in a request.
 *
 * @author Guy Rixon
 */
public class BinaryHttpTester extends HttpTester {

  /**
   * Sets the binary content of a request message.
   * This method is based on setContent(String) in the super-class.
   */
  public void setContent(byte[] content) {
    _parsedContent=null;
    if (content != null) {
      _genContent = content;
      setLongHeader(HttpHeaders.CONTENT_LENGTH, _genContent.length);
    }
    else {
      removeHeader(HttpHeaders.CONTENT_LENGTH);
      _genContent = null;
    }
  }

}
