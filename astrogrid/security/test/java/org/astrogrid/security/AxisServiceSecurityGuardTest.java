package org.astrogrid.security;

import junit.framework.TestCase;


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

  /**
   * Tests the derivation from the message context when the
   * context is empty. This exercises the error handling.
   */
  public void testFactory() throws Exception {
    AxisServiceSecurityGuard g = AxisServiceSecurityGuard.getInstanceFromContext();
    assertNotNull("Guard is not null", g);
    assertTrue(g.isAnonymous());
  }

}