/*
 * CertificateAuthorityBeanTest.java
 * JUnit based test
 *
 * Created on August 11, 2006, 1:52 PM
 */

package org.astrogrid.community.webapp;

import junit.framework.*;
import java.io.File;
import org.apache.log4j.Logger;
import org.astrogrid.community.server.ca.CertificateAuthority;
import org.astrogrid.community.server.ca.UserFiles;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

/**
 *
 * @author guy
 */
public class CertificateAuthorityBeanTest extends TestCase {
  
  public CertificateAuthorityBeanTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(CertificateAuthorityBeanTest.class);
    
    return suite;
  }

  /**
   * Test of getEnablementResult method, of class org.astrogrid.community.webapp.CertificateAuthorityBean.
   */
  public void testGetEnablementResult() {
    System.out.println("getEnablementResult");
    
    CertificateAuthorityBean instance = new CertificateAuthorityBean();
    
    String expResult = "";
    String result = instance.getEnablementResult();
    assertEquals(expResult, result);
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setUserLoginName method, of class org.astrogrid.community.webapp.CertificateAuthorityBean.
   */
  public void testSetUserLoginName() {
    System.out.println("setUserLoginName");
    
    String name = "fred";
    CertificateAuthorityBean instance = new CertificateAuthorityBean();
    
    instance.setUserLoginName(name);
    
    assertEquals(name, instance.getUserLoginName());
  }


  /**
   * Test of setUserCommonName method, of class org.astrogrid.community.webapp.CertificateAuthorityBean.
   */
  public void testSetUserCommonName() {
    System.out.println("setUserCommonName");
    
    String name = "Bill Gates";
    CertificateAuthorityBean instance = new CertificateAuthorityBean();
    
    instance.setUserCommonName(name);
    
    assertEquals(name, instance.getUserCommonName());
  }

  /**
   * Test of setUserNewPassword method, of class org.astrogrid.community.webapp.CertificateAuthorityBean.
   */
  public void testSetUserNewPassword() {
    System.out.println("setUserNewPassword");
    
    String password = "";
    CertificateAuthorityBean instance = new CertificateAuthorityBean();
    
    instance.setUserNewPassword(password);
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setUserOldPassword method, of class org.astrogrid.community.webapp.CertificateAuthorityBean.
   */
  public void testSetUserOldPassword() {
    System.out.println("setUserOldPassword");
    
    String password = "";
    CertificateAuthorityBean instance = new CertificateAuthorityBean();
    
    instance.setUserOldPassword(password);
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getInitiationResult method, of class org.astrogrid.community.webapp.CertificateAuthorityBean.
   */
  public void testGetInitiationResult() {
    System.out.println("getInitiationResult");
    
    CertificateAuthorityBean instance = new CertificateAuthorityBean();
    
    String expResult = "";
    String result = instance.getInitiationResult();
    assertEquals(expResult, result);
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getPasswordChangeResult method, of class org.astrogrid.community.webapp.CertificateAuthorityBean.
   */
  public void testGetPasswordChangeResult() throws Exception {
    System.out.println("getPasswordChangeResult");
    
    CertificateAuthorityBean instance = new CertificateAuthorityBean();
    
    String expResult = "";
    String result = instance.getPasswordChangeResult();
    assertEquals(expResult, result);
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
