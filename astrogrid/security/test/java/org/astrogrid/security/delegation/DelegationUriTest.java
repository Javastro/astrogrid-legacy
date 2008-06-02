package org.astrogrid.security.delegation;

import junit.framework.TestCase;
import org.astrogrid.security.delegation.*;

/**
 * JUnit tests for DelegationUri.
 *
 * @author Guy Rixon
 */
public class DelegationUriTest extends TestCase {
  
  public void testListRequest1() throws Exception {
    DelegationUri sut = new DelegationUri("");
    assertTrue(sut.isValid());
    assertNull(sut.getUser());
    assertEquals(sut.LIST, sut.getResourceCode());
  }
  
  public void testListRequest2() throws Exception {
    DelegationUri sut = new DelegationUri("/");
    assertTrue(sut.isValid());
    assertNull(sut.getUser());
    assertEquals(sut.LIST, sut.getResourceCode());
  }
  
  public void testIdentityRequest1() throws Exception {
    DelegationUri sut = new DelegationUri("/12345");
    assertTrue(sut.isValid());
    assertNotNull(sut.getUser());
    assertEquals("12345", sut.getUser());
    assertEquals(sut.IDENTITY, sut.getResourceCode());
  }
  
  public void testIdentityRequest2() throws Exception {
    DelegationUri sut = new DelegationUri("/12345/");
    assertTrue(sut.isValid());
    assertNotNull(sut.getUser());
    assertEquals("12345", sut.getUser());
    assertEquals(sut.IDENTITY, sut.getResourceCode());
  }
  
  public void testCsrRequest() throws Exception {
    DelegationUri sut = new DelegationUri("/12345/CSR");
    assertTrue(sut.isValid());
    assertNotNull(sut.getUser());
    assertEquals("12345", sut.getUser());
    assertEquals(sut.CSR, sut.getResourceCode());
  }
  
  public void testCertificateRequest() throws Exception {
    DelegationUri sut = new DelegationUri("/12345/certificate");
    assertTrue(sut.isValid());
    assertNotNull(sut.getUser());
    assertEquals("12345", sut.getUser());
    assertEquals(sut.CERTIFICATE, sut.getResourceCode());
  }
  
  public void testDuffRequest1() throws Exception {
    DelegationUri sut = new DelegationUri("/12345/wibble");
    assertFalse(sut.isValid());
    assertNotNull(sut.getUser());
    assertEquals("12345", sut.getUser());
    assertEquals(sut.UNKNOWN, sut.getResourceCode());
  }
  
  public void testDuffRequest2() throws Exception {
    DelegationUri sut = new DelegationUri("/12345/CSR/spurious");
    assertFalse(sut.isValid());
    assertNotNull(sut.getUser());
    assertEquals("12345", sut.getUser());
    assertEquals(sut.UNKNOWN, sut.getResourceCode());
  }
  
}
