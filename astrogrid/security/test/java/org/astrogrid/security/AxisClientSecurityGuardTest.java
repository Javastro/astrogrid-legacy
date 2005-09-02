package org.astrogrid.security;

import junit.framework.TestCase;


/**
 * JUnit tests for {@link org.astrogrid.security.AxisClientSecurityGuard}.
 *
 * @author Guy Rixon
 */
public class AxisClientSecurityGuardTest extends TestCase {

  public void testConstructor() throws Exception {
    AxisClientSecurityGuard g = new AxisClientSecurityGuard();
    assertNotNull(g);
  }

}