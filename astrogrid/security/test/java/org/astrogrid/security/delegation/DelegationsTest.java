/*
 * DelegationsTest.java
 * JUnit based test
 *
 * Created on March 18, 2007, 5:20 PM
 */

package org.astrogrid.security.delegation;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;

/**
 *
 * @author guy
 */
public class DelegationsTest extends TestCase {
  
  private String testIdentity;
  
  protected void setUp() throws Exception {
    String i = "C=UK,O=AstroGrid,OU=Cambridge,CN=Fred Hoyle";
    this.testIdentity = Delegations.getInstance().initializeIdentity(i);
  }

  /**
   * Test of getInstance method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  public void testGetInstance() {
    System.out.println("getInstance");
    
    Delegations expResult = null;
    Delegations result = Delegations.getInstance();
    assertNotNull(result);
  }

  /**
   * Test of initializeIdentity method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  public void testInitializeIdentity() throws Exception {
    System.out.println("initializeIdentity");
    
    String identity = "C=UK,O=AstroGrid,OU=Cambridge,CN=Guy Rixon";
    Delegations instance = Delegations.getInstance();
    
    X500Principal p = new X500Principal(identity);
    String expResult = instance.hash(p);
    String result = instance.initializeIdentity(identity);
    assertEquals(expResult, result);
  }


  /**
   * Test of getCsr method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  public void testGetCsr() throws Exception {
    System.out.println("getCsr");
    
    String identity = "C=UK,O=AstroGrid,OU=Cambridge,CN=Guy Rixon";
    X500Principal p1 = new X500Principal(identity);
    
    Delegations instance = Delegations.getInstance();
    String hashCode = instance.initializeIdentity(p1);
    CertificateSigningRequest result = instance.getCsr(hashCode);
    assertNotNull(result);
    
    System.out.println(result.getCertificationRequestInfo().getSubject());
    X500Principal p2 = new X500Principal(result.getCertificationRequestInfo().getSubject().toString());
    assertEquals(p1.getName(X500Principal.CANONICAL), p2.getName(X500Principal.CANONICAL));
  }

  /**
   * Test of getPrivateKey method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  public void testGetPrivateKey() {
    System.out.println("getPrivateKey");
    
    Delegations instance = Delegations.getInstance();
    Object result = instance.getPrivateKey(this.testIdentity);
    assertNotNull("Private key exists", result);
  }

  /**
   * Test of getCertificate method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  public void testCertificate() throws Exception {
    System.out.println("getCertificate");
    
    // Initially, there should be no certificate.
    Delegations instance = Delegations.getInstance();
    X509Certificate result = instance.getCertificate(this.testIdentity);
    assertNull("No certificate until client puts one.", result);

    SelfSignedCertificateFactory sscf = new SelfSignedCertificateFactory();
    KeyPair keys = sscf.generateKeyPair();
    X509Certificate c1 = sscf.generateCertificate(keys,
                                                  instance.getName(testIdentity),
                                                  200000);
    
    instance.setCertificate(this.testIdentity, c1);
    X509Certificate c2 = instance.getCertificate(this.testIdentity);
    
    assertNotNull(c2);
    assertEquals(c1, c2);
  }
  
  /**
   * Test of getPrincipals method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  public void testGetPrincipals() {
    System.out.println("getPrincipals");
    
    Delegations instance = Delegations.getInstance();
    Object[] result = instance.getPrincipals();
    assertNotNull(result);
    assertTrue(result.length == 2);
  }
  
  /**
   * Test of isKnown method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  public void testIsKnownAndRemove() {
    System.out.println("isKnown and remove");
    
    String bogusIdentity = "C=NG,O=Dodgy,CN=Bogus";
    Delegations instance = Delegations.getInstance();
    assertTrue(instance.isKnown(this.testIdentity));
    assertFalse(instance.isKnown(bogusIdentity));
    instance.remove(this.testIdentity);
    assertFalse(instance.isKnown(this.testIdentity));
  }
  
}
