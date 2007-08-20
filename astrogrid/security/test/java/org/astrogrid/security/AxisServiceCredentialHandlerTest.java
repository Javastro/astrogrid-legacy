package org.astrogrid.security;

import junit.framework.TestCase;

/**
 * JUnit tests for AxisServiceCredentialHandler.
 *
 * @author Guy Rixon
 */
public class AxisServiceCredentialHandlerTest extends TestCase {
  
  public void testInit() throws Exception {
    AxisServiceCredentialHandler sut = new AxisServiceCredentialHandler();
    sut.init();
  }
  
}
