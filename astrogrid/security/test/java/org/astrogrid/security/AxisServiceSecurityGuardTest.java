package org.astrogrid.security;

import java.security.Principal;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.server.AxisServer;


/**
 * JUnit tests for {@link org.astrogrid.security.AxisServiceSecurityGuard}.
 *
 * @author Guy Rixon
 */
public class AxisServiceSecurityGuardTest extends TestCase {

  /**
   * Tests the no-argument constructor.
   */
  public void testConstruction() throws Exception {
    AxisServiceSecurityGuard g = new AxisServiceSecurityGuard();
    assertTrue(g.isAnonymous());
  }
  
  public void testConstructionFromContextAnonymous() throws Exception {
    AxisServer s = new AxisServer();
    MessageContext m = new MessageContext(s);
    AxisServiceSecurityGuard sut = new AxisServiceSecurityGuard(m);
    assertNotNull(sut);
    assertTrue(sut.isAnonymous());
  }
  
  public void testConstructionFromContextAuthenticated() throws Exception {
    AxisServer s = new AxisServer();
    MessageContext m = new MessageContext(s);
    AxisServiceSecurityGuard g = new AxisServiceSecurityGuard();
    g.setX500Principal(new X500Principal("o=foo,cn=bar"));
    m.setProperty(AxisServiceSecurityGuard.AXIS_PROPERTY, g);
    assertTrue(m.containsProperty(AxisServiceSecurityGuard.AXIS_PROPERTY));
    AxisServiceSecurityGuard sut = new AxisServiceSecurityGuard(m);
    assertNotNull(sut);
    assertFalse("Anonymous access", sut.isAnonymous());
  }

  /**
   * Tests the derivation from the message context when the
   * context is empty. This exercises the error handling.
   */
  public void testFactoryAnonymous() throws Exception {
    AxisServiceSecurityGuard g = AxisServiceSecurityGuard.getInstanceFromContext();
    assertNotNull("Guard is not null", g);
    assertTrue(g.isAnonymous());
  }
  
  public void testFactoryAuthenticated() throws Exception {
    AxisServiceSecurityGuard.loadFakeUserFred();
    AxisServiceSecurityGuard g = AxisServiceSecurityGuard.getInstanceFromContext();
    assertNotNull("Guard is not null", g);
    for (Principal p : g.getSubject().getPrincipals()) {
      System.out.println(p);
    }
    assertFalse("Anonymous access", g.isAnonymous());
    assertEquals(new X500Principal("o=ioa,cn=fred hoyle"), g.getX500Principal());
  }

}