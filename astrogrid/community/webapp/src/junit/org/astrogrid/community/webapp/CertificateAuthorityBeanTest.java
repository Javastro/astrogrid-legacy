package org.astrogrid.community.webapp;

import junit.framework.TestCase;

/**
 * JUnit tests for CertificateAuthorityBean.
 *
 * @author Guy Rixon
 */
public class CertificateAuthorityBeanTest extends TestCase {
  
  public void testState() throws Exception {
    
    CertificateAuthorityBean sut = new CertificateAuthorityBean();
    
    // Expect the CA to be disabled when the bean is first constructed.
    assertEquals("disabled", sut.getCaState());
    
    // After passing it an incorrect password, the CA should still be disabled.
    sut.setCaPassword("NBG");
    assertEquals("disabled", sut.getCaState());
  }
  
}
