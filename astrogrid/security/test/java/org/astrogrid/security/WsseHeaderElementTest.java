package org.astrogrid.security;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import junit.framework.TestCase;


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

    WsseHeaderElement.write(sg, sm);
    String soapSerialized = sm.getSOAPPart().getEnvelope().toString();
    System.out.println(soapSerialized);

    // Check the structure of the XML.
    XmlChecker xc = this.getXmlChecker(soapSerialized);
    assertTrue(0 < xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
    assertEquals(1, xc.countMatchingNodes("//wsse:Security/wsse:UsernameToken"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Username"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Password"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Nonce"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wssu:Created"));
  }


  /**
   * Tests the serializer when username only is in use.
   */
  public void testSerializerUsernameOnly () throws Exception {
    SecurityGuard sg = new SecurityGuard();
    sg.setUsername("Fred");

    SOAPMessage sm = MessageFactory.newInstance().createMessage();

    WsseHeaderElement.write(sg, sm);
    String soapSerialized = sm.getSOAPPart().getEnvelope().toString();
    System.out.println(soapSerialized);

    // Check the structure of the XML.
    XmlChecker xc = this.getXmlChecker(soapSerialized);
    assertTrue(0 < xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
    assertEquals(1, xc.countMatchingNodes("//wsse:Security/wsse:UsernameToken"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Username"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Password"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Nonce"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wssu:Created"));
  }


  /**
   * Tests the serializer when no credentials are present.
   */
  public void testSerializerBlank () throws Exception {
    SecurityGuard sg = new SecurityGuard();

    SOAPMessage sm = MessageFactory.newInstance().createMessage();

    WsseHeaderElement.write(sg, sm);

    String soapSerialized = sm.getSOAPPart().getEnvelope().toString();
    System.out.println(soapSerialized);

    // Check the structure of the XML.
    XmlChecker xc = this.getXmlChecker(soapSerialized);
    assertTrue(0 < xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
    assertEquals(1, xc.countMatchingNodes("//wsse:Security/wsse:UsernameToken"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Username"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Password"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Nonce"));
    assertEquals(0, xc.countMatchingNodes("//wsse:UsernameToken/wssu:Created"));
  }


  /**
   * Tests the serializer when username and hashed password are in use.
   */
  public void testSerializerUsernameHashedPassword () throws Exception {
    SecurityGuard sg = new SecurityGuard();
    sg.setUsername("Fred");
    sg.setPassword("secret");
    sg.setPasswordHashing(true);

    SOAPMessage sm = MessageFactory.newInstance().createMessage();

    WsseHeaderElement.write(sg, sm);
    String soapSerialized = sm.getSOAPPart().getEnvelope().toString();
    System.out.println(soapSerialized);

    // Check the structure of the XML.
    XmlChecker xc = this.getXmlChecker(soapSerialized);
    assertTrue(0 < xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
    assertEquals(1, xc.countMatchingNodes("//wsse:Security/wsse:UsernameToken"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Username"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Password"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Password/@Type"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Nonce"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wssu:Created"));
  }


  /**
   * Tests the parsing when a username and clear-text password are in use.
   */
  public void testParserUsernamePassword () throws Exception {
    SecurityGuard sg1 = new SecurityGuard();
    sg1.setUsername("Fred");
    sg1.setPassword("secret");

    SOAPMessage sm = MessageFactory.newInstance().createMessage();
    WsseHeaderElement.write(sg1, sm);

    SecurityGuard sg2 = new SecurityGuard();
    WsseHeaderElement.parse(sm, sg2);

    assertNotNull(sg2.getUsername());
    System.out.println("Username " + sg2.getUsername());
    assertTrue(sg2.getUsername().equals(sg1.getUsername()));

    assertNotNull(sg2.getPassword());
    System.out.println("Password " + sg2.getPassword());
    assertTrue(sg2.getPassword().equals(sg1.getPassword()));

    assertFalse(sg2.isPasswordHashing());
  }


    /**
     * Tests the parsing when a username and hashed password are in use.
     */
    public void testParserUsernameHashedPassword () throws Exception {
      SecurityGuard sg1 = new SecurityGuard();
      sg1.setUsername("Fred");
      sg1.setPassword("secret");
      sg1.setPasswordHashing(true);

      SOAPMessage sm = MessageFactory.newInstance().createMessage();
      WsseHeaderElement.write(sg1, sm);

      SecurityGuard sg2 = new SecurityGuard();
      WsseHeaderElement.parse(sm, sg2);

      assertNotNull(sg2.getUsername());
      System.out.println("Username " + sg2.getUsername());
      assertTrue(sg2.getUsername().equals(sg1.getUsername()));

      assertNotNull(sg2.getPassword());
      System.out.println("Password " + sg2.getPassword());

      assertTrue(sg2.isPasswordHashing());
  }


  /**
   * Returns an XmlChecker initialized with the right namespaces for these tests.
   */
  private XmlChecker getXmlChecker (String xml) throws Exception {
    XmlChecker xc = new XmlChecker(xml);
    xc.addXpathNamespace("soapenv",
                         "http://schemas.xmlsoap.org/soap/envelope/");
    xc.addXpathNamespace("wsse",
                         "http://schemas.xmlsoap.org/ws/2002/07/secext");
    xc.addXpathNamespace("wssu",
                         "http://schemas.xmlsoap.org/ws/2002/07/utility");
    return xc;
  }

}