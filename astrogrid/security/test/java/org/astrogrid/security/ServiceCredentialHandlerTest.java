package org.astrogrid.security;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import junit.framework.TestCase;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate;


/**
 * JUnit tests for {@link ServiceCredentialHandler}.
 *
 * @author Guy Rixon
 */
public class ServiceCredentialHandlerTest extends TestCase {

  /**
   * Tests authentication from a message with valid credentials.
   * The credentials are an AstroGrid NonceToken.
   */
  public void testWithValidCredential () throws Exception {

    // Define the credentials.
    Subject s1 = new Subject();
    String account = "ivo://org.astrogrid.mock/Fred";
    s1.getPrincipals().add(new AccountName(account));
    SecurityServiceMockDelegate ssd = new SecurityServiceMockDelegate();
    NonceToken n1 = new NonceToken(ssd.checkPassword(account, "secret"));
    s1.getPrivateCredentials().add(n1);

    // Create and initialize the handler.
    ServiceCredentialHandler h = new ServiceCredentialHandler();
    h.init(this.createHandlerInfo());

    // Create a context to be handled in which there is a
    // SOAP message carrying the crednetials.
    SOAPMessageContext smc = new DummyMessageContext();
    SOAPMessage msg = MessageFactory.newInstance().createMessage();
    assertNotNull(msg);
    WsseHeaderElement.write(s1, msg);
    smc.setMessage(msg);

    // Invoke the handler.
    h.handleRequest(smc);

    // Check that the handler left the results of authentication
    // in the message context.
    Object o = smc.getProperty("Subject");
    assertNotNull("Subject property exists", o);
    assertTrue("Value of Subject property is a Subject",
               (o instanceof Subject));
    Subject s2 = (Subject) o;
    assertTrue("Subject contains a name",
               s2.getPrincipals().size() > 0);
    Set tokens = s2.getPrivateCredentials(NonceToken.class);
    assertEquals("Exactly one token", 1, tokens.size());
    NonceToken n2 = (NonceToken) tokens.iterator().next();
    assertTrue("Token is valid", n2.isValid());
  }


  /**
   * Generates a HandlerInfo object set up for the SUT.
   */
  private HandlerInfo createHandlerInfo () throws Exception {
    Map m = new Hashtable();
    HandlerInfo hi = new HandlerInfo();
    hi.setHandlerConfig(m);
    return hi;
  }
}
