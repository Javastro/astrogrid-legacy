package org.astrogrid.security.delegation;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit-4 tests for {@link Delegations}.
 * @author Guy Rixon
 */
public class DelegationsTest {
  
  private String testIdentity;

  @BeforeClass public static void erase() {
    System.out.println("erasing");
    Delegations.getInstance().erase();
  }

  @Before public void identity() throws Exception {
    System.out.println("Initializing identity");
    String i = "C=UK,O=AstroGrid,OU=Cambridge,CN=Fred Hoyle";
    this.testIdentity = Delegations.getInstance().initializeIdentity(i);
  }

  /**
   * Test of getInstance method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  @Test public void testGetInstance() {
    System.out.println("getInstance");
    
    Delegations result = Delegations.getInstance();
    assertNotNull(result);
  }

  /**
   * Test of initializeIdentity method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  @Test public void testInitializeIdentity() throws Exception {
    System.out.println("initializeIdentity");
    
    String identity = "C=UK,O=AstroGrid,OU=Cambridge,CN=Guy Rixon";
    Delegations instance = Delegations.getInstance();
    
    X500Principal p = new X500Principal(identity);
    String expResult = instance.hash(p);
    String hash1 = instance.initializeIdentity(identity);
    assertEquals(expResult, hash1);
    KeyPair k1 = instance.getKeys(hash1);

    // Initializing an identity must be idempotent.
    String hash2 = instance.initializeIdentity(identity);
    assertEquals(hash1, hash2);
    KeyPair k2 = instance.getKeys(hash2);
    assertEquals(k1, k2);
  }


  /**
   * Test of getCsr method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  @Test public void testGetCsr() throws Exception {
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
  @Test public void testGetPrivateKey() {
    System.out.println("getPrivateKey");
    
    Delegations instance = Delegations.getInstance();
    Object result = instance.getPrivateKey(this.testIdentity);
    assertNotNull("Private key missing", result);
  }

  /**
   * Test of getCertificate method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  @Test public void testCertificate() throws Exception {
    System.out.println("getCertificate");
    
    // Initially, there should be no certificate.
    Delegations instance = Delegations.getInstance();
    X509Certificate result = instance.getCertificate(testIdentity);
    assertNull("No certificate until client puts one.", result);

    SelfSignedCertificateFactory sscf = new SelfSignedCertificateFactory();
    X509Certificate c1 = sscf.generateCertificate(instance.getKeys(testIdentity),
                                                  instance.getName(testIdentity),
                                                  200000);
    assertNotNull(c1);
    assertNotNull(testIdentity);
    instance.setCertificate(testIdentity, c1);

    X509Certificate c2 = instance.getCertificate(testIdentity);
    
    assertNotNull(c2);
    assertEquals(c1, c2);
  }
  
  /**
   * Test of getPrincipals method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  @Test public void testGetPrincipals() {
    System.out.println("getPrincipals");
    
    Delegations instance = Delegations.getInstance();
    Object[] result = instance.getPrincipals();
    assertNotNull(result);
    System.out.println(String.format("%d principals", result.length));
    assertTrue(result.length == 2);
  }
  
  /**
   * Test of isKnown method, of class org.astrogrid.security.delegation.DelegationRecords.
   */
  @Test public void testIsKnownAndRemove() {
    System.out.println("isKnown and remove");
    
    String bogusIdentity = "C=NG,O=Dodgy,CN=Bogus";
    Delegations instance = Delegations.getInstance();
    assertTrue(instance.isKnown(this.testIdentity));
    assertFalse(instance.isKnown(bogusIdentity));
    instance.remove(this.testIdentity);
    assertFalse(instance.isKnown(this.testIdentity));
  }
  
}
