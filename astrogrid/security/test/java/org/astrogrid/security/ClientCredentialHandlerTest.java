package org.astrogrid.security;

import java.util.Hashtable;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import junit.framework.TestCase;

/**
 * JUnit tests for {@link ClientCredentialHandler}.
 *
 * @author Guy Rixon
 */
public class ClientCredentialHandlerTest extends TestCase {

  /**
   * Constructs the test case.
   */
  public ClientCredentialHandlerTest () {
    super();
  }


  /**
   * Tests the report of the set of headers handled.
   * Requires that at least one type of header be handled.
   */
  public void testGetHeaders () throws Exception {
    ClientCredentialHandler h = new ClientCredentialHandler();
    QName[] headerNames = h.getHeaders();
    assertNotNull(headerNames);
    assertTrue(headerNames.length > 0);
  }


  /**
   * Tests handling of outgoing messages.
   */
  public void testHandleRequestWithInvalidContext () throws Exception {
    ClientCredentialHandler h = new ClientCredentialHandler();
    h.init(this.createHandlerInfo());

    // Invoke the handler with a message context that is not
    // a SOAP message context; expect an exception.
    try {
      MessageContext mc = new DummyMessageContext();
      h.handleRequest(mc);
      fail("Handler did not throw an exception for an invalid context.");
    }
    catch (Exception e1) {
      System.out.println(e1);
    }
  }


  /**
   * Tests handling of outgoing messages.
   */
  public void testHandleRequestWithEmptyContext () throws Exception {
    ClientCredentialHandler h = new ClientCredentialHandler();
    h.init(this.createHandlerInfo());
    try {
      SOAPMessageContext smc = new DummyMessageContext();
      h.handleRequest(smc);
      fail("Handler did not throw an exception for an invalid context.");
    }
    catch (Exception e2) {
      System.out.println(e2);
    }
  }


  /**
   * Tests handling of outgoing messages.
   */
  public void testHandleRequestWithValidContext () throws Exception {
    ClientCredentialHandler h = new ClientCredentialHandler();
    h.init(this.createHandlerInfo());
    SOAPMessageContext smc = new DummyMessageContext();
    SOAPMessage msg = MessageFactory.newInstance().createMessage();
    assertNotNull(msg);
    smc.setMessage(msg);
    h.handleRequest(smc);

    // Check that the handler wrote a SOAP header.
    String xml = smc.getMessage().getSOAPPart().getEnvelope().toString();
    XmlChecker xc = new XmlChecker(xml);
    xc.addXpathNamespace("soapenv",
                         "http://schemas.xmlsoap.org/soap/envelope/");
    xc.addXpathNamespace("wsse",
                         "http://schemas.xmlsoap.org/ws/2002/07/secext");
    assertTrue(0 < xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
  }


  /**
   * Generates a HandlerInfo object set up for the SUT.
   */
  private HandlerInfo createHandlerInfo () {
    SecurityGuard sg = new SecurityGuard();
    sg.setUsername("Fred");
    sg.setPassword("secret");
    Map m = new Hashtable();
    m.put("SecurityGuard", sg);
    HandlerInfo hi = new HandlerInfo();
    hi.setHandlerConfig(m);
    return hi;
  }

}