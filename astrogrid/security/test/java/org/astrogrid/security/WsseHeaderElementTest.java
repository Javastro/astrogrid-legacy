package org.astrogrid.security;

import java.security.Principal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
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
    Subject subject = new Subject();
    AccountName account = new AccountName("Fred");
    subject.getPrincipals().add(account);
    Password password = new Password("secret", false);
    subject.getPrivateCredentials().add(password);

    SOAPMessage message = MessageFactory.newInstance().createMessage();

    WsseHeaderElement.write(subject, message);
    String soapSerialized = message.getSOAPPart().getEnvelope().toString();
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
    Subject subject = new Subject();
    AccountName account = new AccountName("Fred");
    subject.getPrincipals().add(account);

    SOAPMessage message = MessageFactory.newInstance().createMessage();

    WsseHeaderElement.write(subject, message);
    String soapSerialized = message.getSOAPPart().getEnvelope().toString();
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
    Subject subject = new Subject();

    SOAPMessage message = MessageFactory.newInstance().createMessage();

    WsseHeaderElement.write(subject, message);

    String soapSerialized = message.getSOAPPart().getEnvelope().toString();
    System.out.println(soapSerialized);

    // Check the structure of the XML.
    XmlChecker xc = this.getXmlChecker(soapSerialized);
    assertEquals(1, xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
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
    Subject subject = new Subject();
    AccountName account = new AccountName("Fred");
    subject.getPrincipals().add(account);
    Password password = new Password("secret", true);
    subject.getPrivateCredentials().add(password);

    SOAPMessage message = MessageFactory.newInstance().createMessage();

    WsseHeaderElement.write(subject, message);

    String soapSerialized = message.getSOAPPart().getEnvelope().toString();
    System.out.println("\n" + soapSerialized);

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
   * Tests the serialization of an AstroGrid SecurityToken
   * (one-time-password type) to WSSE. A wsse:UsernameToken should be
   * created. The token as a whole should go into the password field
   * and the account name into the Username field.
   */
  public void testSerializerWithNonceToken () throws Exception {
    System.out.println();
    Subject subject = new Subject();
    NonceToken token = new NonceToken("Fred", "etc");
    System.out.println("Account name in token: " + token.getAccount());
    subject.getPrivateCredentials().add(token);
    subject.getPrincipals().add(new AccountName("Fred"));

    SOAPMessage message = MessageFactory.newInstance().createMessage();

    WsseHeaderElement.write(subject, message);
    String soapSerialized = message.getSOAPPart().getEnvelope().toString();
    System.out.println(soapSerialized);

    // Check the structure of the XML.
    XmlChecker xc = this.getXmlChecker(soapSerialized);
    assertEquals(1, xc.countMatchingNodes("//soapenv:Header/wsse:Security"));
    assertEquals(1, xc.countMatchingNodes("//wsse:Security/wsse:UsernameToken"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/ag:NonceToken"));
    assertEquals(1, xc.countMatchingNodes("//wsse:UsernameToken/wsse:Username"));
  }



  /**
   * Tests the parsing when a username and clear-text password are in use.
   */
  public void testParserUsernamePassword () throws Exception {
    Subject s1 = new Subject();
    s1.getPrincipals().add(new AccountName("Fred"));
    s1.getPrivateCredentials().add(new Password("secret", false));

    SOAPMessage sm = MessageFactory.newInstance().createMessage();
    WsseHeaderElement.write(s1, sm);

    Subject s2 = new Subject();
    WsseHeaderElement.parse(sm, s2);

    Set principals = s2.getPrincipals();
    assertEquals("Number of principals",
                 1, principals.size());
    assertEquals("Value of principal",
                 "Fred", ((Principal) principals.iterator().next()).getName());

    Set passwords = s2.getPrivateCredentials(Password.class);
    assertEquals("Number of passwords", 1, passwords.size());

    Password p = (Password) passwords.iterator().next();
    System.out.println("Password " + p.getPlainPassword());
    assertTrue(p.getPlainPassword().equals("secret"));
    assertFalse(p.isEncodable());
  }


  /**
   * Tests the parsing when a username and hashed password are in use.
   */
  public void testParserUsernameHashedPassword () throws Exception {
    Subject s1 = new Subject();
    s1.getPrincipals().add(new AccountName("Fred"));
    s1.getPrivateCredentials().add(new Password("secret", true));

    SOAPMessage sm = MessageFactory.newInstance().createMessage();
    WsseHeaderElement.write(s1, sm);

    Subject s2 = new Subject();
    WsseHeaderElement.parse(sm, s2);

    Set principals = s2.getPrincipals();
    assertEquals("Number of principals",
                 1, principals.size());
    assertEquals("Value of principal",
                 "Fred", ((Principal) principals.iterator().next()).getName());

    Set passwords = s2.getPrivateCredentials(Password.class);
    assertEquals("Number of passwords", 1, passwords.size());
  }


  /**
   * Tests the parsing when am AstroGrid security token is in use.
   */
  public void testParserWithNonceToken () throws Exception {
    Subject s1 = new Subject();
    NonceToken t1 = new NonceToken("Fred", "Secret");
    s1.getPrivateCredentials().add(t1);
    s1.getPrincipals().add(new AccountName("Fred"));

    Subject s2 = new Subject();

    SOAPMessage sm = MessageFactory.newInstance().createMessage();
    WsseHeaderElement.write(s1, sm);
    WsseHeaderElement.parse(sm, s2);

    Set tokens = s2.getPrivateCredentials(NonceToken.class);
    assertEquals("Exactly one token", 1, tokens.size());

    Set names = s2.getPrincipals();
    assertEquals("Exactly one name", 1, names.size());
    AccountName n = (AccountName) names.iterator().next();
    assertEquals("Names match", "Fred", n.getName());
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
    xc.addXpathNamespace("ag",
                         "urn:astrogrid:security:wsse");
    return xc;
  }

}