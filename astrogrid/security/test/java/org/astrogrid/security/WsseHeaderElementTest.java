package org.astrogrid.security;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import junit.framework.TestCase;
import org.dom4j.DocumentHelper;
import org.dom4j.xpath.DefaultXPath;


/**
 * Unit tests for {@link WsseHeaderElement}.
 *
 * @author Guy Rixon
 */
public class WsseHeaderElementTest extends TestCase {

  /**
   * Tests the serializer when username and password are in use.
   */
  public void testSerializerUsernamePassword () throws Exception {
    SecurityGuard sg = new SecurityGuard();
    sg.setUsername("Fred");
    sg.setPassword("secret");

    SOAPMessage sm = MessageFactory.newInstance().createMessage();

    WsseHeaderElement he = new WsseHeaderElement(sg, sm);
    he.write();
    String soapSerialized = sm.getSOAPPart().getEnvelope().toString();
    System.out.println(soapSerialized);

    // Check the structure of the XML.
    XmlChecker xc = new XmlChecker(soapSerialized);
    xc.addXpathNamespace("soapenv",
                         "http://schemas.xmlsoap.org/soap/envelope/");
    xc.addXpathNamespace("wsse",
                         "http://schemas.xmlsoap.org/ws/2002/04/secext");
    assertTrue(0 < xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
    assertTrue(0 < xc.countMatchingNodes("//wsse:Security/wsse:UsernameToken"));
    assertTrue(0 < xc.countMatchingNodes("//wsse:UsernameToken/wsse:Username"));
    assertTrue(0 < xc.countMatchingNodes("//wsse:UsernameToken/wsse:Password"));
  }


    /**
     * Tests the serializer when username only is in use.
     */
    public void testSerializerUsernameOnly () throws Exception {
      SecurityGuard sg = new SecurityGuard();
      sg.setUsername("Fred");

      SOAPMessage sm = MessageFactory.newInstance().createMessage();

      WsseHeaderElement he = new WsseHeaderElement(sg, sm);
      he.write();
      String soapSerialized = sm.getSOAPPart().getEnvelope().toString();
      System.out.println(soapSerialized);

      // Check the structure of the XML.
      XmlChecker xc = new XmlChecker(soapSerialized);
      xc.addXpathNamespace("soapenv",
                           "http://schemas.xmlsoap.org/soap/envelope/");
      xc.addXpathNamespace("wsse",
                           "http://schemas.xmlsoap.org/ws/2002/04/secext");
      assertTrue(0 < xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
      assertTrue(0 < xc.countMatchingNodes("//wsse:Security/wsse:UsernameToken"));
      assertTrue(0 < xc.countMatchingNodes("//wsse:UsernameToken/wsse:Username"));
      assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Password"));
  }


      /**
       * Tests the serializer when no credentials are present.
       */
      public void testSerializerBlank () throws Exception {
        SecurityGuard sg = new SecurityGuard();

        SOAPMessage sm = MessageFactory.newInstance().createMessage();

        WsseHeaderElement he = new WsseHeaderElement(sg, sm);
        he.write();
        String soapSerialized = sm.getSOAPPart().getEnvelope().toString();
        System.out.println(soapSerialized);

        // Check the structure of the XML.
        XmlChecker xc = new XmlChecker(soapSerialized);
        xc.addXpathNamespace("soapenv",
                             "http://schemas.xmlsoap.org/soap/envelope/");
        xc.addXpathNamespace("wsse",
                             "http://schemas.xmlsoap.org/ws/2002/04/secext");
        assertTrue(0 < xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
        assertTrue(0 < xc.countMatchingNodes("//wsse:Security/wsse:UsernameToken"));
        assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Username"));
        assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Password"));
  }


}