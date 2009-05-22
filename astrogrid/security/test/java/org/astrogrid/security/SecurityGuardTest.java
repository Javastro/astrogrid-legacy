package org.astrogrid.security;

import java.io.File;
import java.net.URI;
import java.security.AccessControlException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.community.MockRegistry;
import org.astrogrid.security.authorization.AuthenticatedAccessPolicy;
import org.astrogrid.security.community.AccountServlet;
import org.astrogrid.security.community.MockRegistryClient;
import org.astrogrid.security.delegation.Delegations;
import org.astrogrid.security.delegation.SelfSignedCertificateFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * JUnit tests for the {@link SecurityGuard class}.
 * <p>
 * Some of the tests involve calls to the accounts web-service in a
 * community installation. For these, the web-service is run locally, in an
 * embedded copy of Jetty, using a local database of accounts. In order that
 * the software under test find this service, a MockRegistryClient is
 * injected.
 *
 * @author Guy Rixon
 */
public class SecurityGuardTest extends TestCase {
  
  private Server jetty;
  
  @Override
  public void setUp() throws Exception {
    
    // Run the community service in an embedded copy of Jetty, using the
    // DB initialized above.
    jetty = new Server(MockRegistry.ACCOUNTS_PORT);
    Context c = new Context(jetty,"/community",Context.SESSIONS);
    c.addServlet(new ServletHolder(new AccountServlet()), "/accounts/*");
    jetty.start();

    // We don't use a registry service in the test, but the registry gear
    // needs the property to be set.
    SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.endpoint",
                                            "http://nada.nix.not-there/bogus");
  }
  
  @Override
  protected void tearDown() throws Exception {
    jetty.stop();
  }

  /**
   * Tests the ssoUsername property.
   */
  public void testSsoUsername () throws Exception {
    SecurityGuard g = new SecurityGuard();
    assertNull("Null SSO user-name after construction", g.getSsoUsername());
    g.setSsoUsername("fred");
    assertEquals("fred", g.getSsoUsername());
  }

  /**
   * Tests the ssoPassword property.
   */

  public void testSsoPassword () throws Exception {
    SecurityGuard g = new SecurityGuard();
    assertNull("Null SSO password after construction", g.getSsoPassword());
    g.setSsoPassword("secret");
    assertEquals("Password matches", "secret", g.getSsoPassword());
  }
  
  /**
   * Tests the copy constructor.
   */
  public void testCopy() throws Exception {
    X500Principal p1 = new X500Principal("CN=foo");
    SecurityGuard g1 = new SecurityGuard();
    g1.setX500Principal(p1);
    g1.setAccessPolicy(new AuthenticatedAccessPolicy());
    
    SecurityGuard g2 = new SecurityGuard(g1);
    X500Principal p2 = g2.getX500Principal();
    assertEquals("Principals match", p1, p2);
    assertTrue("Access policy was copied correctly",
               g2.accessPolicy instanceof AuthenticatedAccessPolicy);
  }

  /**
   * Tests the sign-on function. This mocks the community.
   */
  public void testSignOn3Argument() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    sut.signOn("ivo://frog@pond/community", "croakcroak", 36000);
    assertEquals("ivo://frog@pond/community", sut.getAccountIvorn().toString());
  }
  
  /**
   * Tests the sign-on function. This mocks the community.
   */
  public void testSignOn4ArgumentCommunity() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI community = new URI("ivo://pond/community");
    sut.signOn("frog", "croakcroak", 36000, community);
    assertEquals("ivo://frog@pond/community", sut.getAccountIvorn().toString());
  }

  /**
   * Tests the sign-on function. This uses a real Myproxy service,
   * accessed by its URL.
   */
  /*
  public void testSignOn4ArgumentMyProxyDirect() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI source = new URI("myproxy://ag03.ast.cam.ac.uk:7512");
    sut.signOn("frog", "croakcroak", 36000, source);
    assertNotNull(sut.getPrivateKey());
    assertNotNull(sut.getX500Principal());
  }
  */

  /**
   * Tests the sign-on function. This uses a real Myproxy service,
   * accessed via its registration.
   */
  /*
  public void testSignOn4ArgumentMyProxyIndirect() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI source = new URI("ivo://pond/myproxy");
    sut.signOn("frog", "croakcroak", 36000, source);
    assertNotNull(sut.getPrivateKey());
    assertNotNull(sut.getX500Principal());
  }
  */
  
  /**
   * Tests the sign-on function. This mocks the community.
   */
  public void testFailedSignOn4ArgumentCommunity() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI community = new URI("ivo://pond/community");
    try {
      sut.signOn("frog", "wrong", 36000, community);
      fail("Should throw exception for wrong password.");
    }
    catch (AccessControlException e) {
      // Expected.
    }
    assertNull(sut.getAccountIvorn());
  }
  
  /**
   * Tests the sign-on function. This uses local credentials in
   * a JKS store.
   */
  public void testSignOn4ArgumentJks() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI keyStore = this.getClass().getResource("/tester.jks").toURI();
    sut.signOn("tester", "testing", 36000, keyStore);
    assertNull(sut.getAccountIvorn());
    assertNotNull(sut.getCertificateChain());
    assertNotNull(sut.getPrivateKey());
    assertNotNull(sut.getX500Principal());
  }

  /**
   * Tests the sign-on function. This uses local credentials
   * in a PKCS#12 store.
   */
  public void testSignOn4ArgumentP12() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI keyStore = this.getClass().getResource("/tester.p12").toURI();
    sut.signOn("tester", "testing", 36000, keyStore);
    assertNull(sut.getAccountIvorn());
    assertNotNull(sut.getCertificateChain());
    assertNotNull(sut.getPrivateKey());
    assertNotNull(sut.getX500Principal());
  }
  
  public void testPasswordChangeInLocalKeyStore() throws Exception {
    File store = TestUtilities.copyResourceToFile(this.getClass(), "/tester.jks");
    SecurityGuard sut = new SecurityGuard();
    sut.changePassword("tester", "testing", "tested", store.toURI());
    sut.signOn("tester", "tested", 3600, store.toURI());
  }
  
  public void testPasswordChangeInRemoteKeyStore() throws Exception {
    URI store = new URI("http://some.where/over/the/rainbow");
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    try {
      sut.changePassword("tester", "testing", "tested", store);
      fail("Should have rejected this password change as impossible.");
    }
    catch (Exception e) {
      // Expected.
    }
  }
  
  public void testPasswordChangeInCommunity() throws Exception {
    URI community = new URI("ivo://pond/community");
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    sut.changePassword("frog", "croakcroak", "ribbitribbit", community);
    sut.signOn("frog", "ribbitribbit", 36000, community);
  }

  public void testIsSignedOnAfterSignOn() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    assertFalse(sut.isSignedOn());
    sut.signOn("tester", "testing", 36000,
                this.getClass().getResource("/tester.jks").toURI());
    assertTrue(sut.isSignedOn());
  }

  public void testIsSignedOnIrregular() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    assertFalse(sut.isSignedOn());

    sut.setX500Principal(new X500Principal("CN=foo"));
    assertFalse(sut.isSignedOn());

    KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    sut.setPrivateKey(keys.getPrivate());
    assertFalse(sut.isSignedOn());

    sut.setCertificateChain(new X509Certificate[0]);
    assertFalse(sut.isSignedOn());

    SelfSignedCertificateFactory sscf = new SelfSignedCertificateFactory();
    X509Certificate x509 = sscf.generateCertificate(keys,
                                                    "CN=foo",
                                                    200000);
    X509Certificate[] chain = new X509Certificate[1];
    chain[0] = x509;
    sut.setCertificateChain(chain);
    assertTrue(sut.isSignedOn());
  }


  public void testLoadDelegation() throws Exception {
    Delegations delegations = Delegations.getInstance();
    delegations.erase();
    SecurityGuard sut = new SecurityGuard();
    assertFalse(sut.isSignedOn());

    // The guard should not be signed on if there are no delegations.
    sut.loadDelegation();
    assertFalse(sut.isSignedOn());

    // The guard should not be signed on if there is a delgated identity
    // and key but no certificate
    X500Principal p = new X500Principal("O=foo, CN=bar");
    String hash = delegations.initializeIdentity(p);
    sut.setX500Principal(p);
    sut.loadDelegation();
    assertFalse(sut.isSignedOn());
    assertTrue(delegations.isKnown(hash));

    // The guard should be signed on if there is a delegated identity,
    // key and proxy certificate.
    SelfSignedCertificateFactory sscf = new SelfSignedCertificateFactory();
    X509Certificate c1 = sscf.generateCertificate(delegations.getKeys(hash),
                                                  delegations.getName(hash),
                                                  200000);
    delegations.setCertificate(hash, c1);
    assertNotNull(delegations.getCertificate(hash));
    sut.loadDelegation();
    assertNotNull(sut.getCertificateChain());
    System.out.println(String.format("%d certificates", sut.getCertificateChain().length));
    assertEquals(1, sut.getCertificateChain().length);
    assertTrue(sut.isSignedOn());
  }

  public void testSetUniquePrincipal() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    assertEquals(0, sut.getSubject().getPrincipals().size());
    assertEquals(0, sut.getSubject().getPrincipals(X500Principal.class).size());

    sut.setUniquePrincipal(new HomespaceLocation("http://where/ever"));
    assertEquals(1, sut.getSubject().getPrincipals().size());

    X500Principal p1 = new X500Principal("CN=foo");
    sut.setUniquePrincipal(p1);
    assertEquals(p1, sut.getX500Principal());
    assertEquals(1, sut.getSubject().getPrincipals(X500Principal.class).size());
    assertEquals(2, sut.getSubject().getPrincipals().size());

    X500Principal p2 = new X500Principal("CN=foo");
    sut.setUniquePrincipal(p2);
    assertEquals(p2, sut.getX500Principal());
    assertEquals(1, sut.getSubject().getPrincipals(X500Principal.class).size());
    assertEquals(2, sut.getSubject().getPrincipals().size());

    assertEquals(1, sut.getSubject().getPrincipals(HomespaceLocation.class).size());
  }

  public void testSetUniquePrivateCredential() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    assertEquals(0, sut.getSubject().getPrivateCredentials().size());

    sut.setUniquePrivateCredential("secret");
    assertEquals(1, sut.getSubject().getPrivateCredentials(String.class).size());

    KeyPair k1 = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    sut.setUniquePrivateCredential(k1.getPrivate());
    assertEquals(k1.getPrivate(), sut.getPrivateKey());
    assertEquals(1, sut.getSubject().getPrivateCredentials(PrivateKey.class).size());
    assertEquals(1, sut.getSubject().getPrivateCredentials(String.class).size());

    KeyPair k2 = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    sut.setUniquePrivateCredential(k2.getPrivate());
    assertEquals(k2.getPrivate(), sut.getPrivateKey());
    assertEquals(1, sut.getSubject().getPrivateCredentials(PrivateKey.class).size());
    assertEquals(1, sut.getSubject().getPrivateCredentials(String.class).size());
  }

  public void testSetUniquePublicCredential() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    assertEquals(0, sut.getSubject().getPublicCredentials().size());

    sut.setUniquePublicCredential("public");
    assertEquals(1, sut.getSubject().getPublicCredentials(String.class).size());

    sut.setUniquePublicCredential(new AccountName("foo"));
    assertEquals("foo", sut.getSsoUsername());
    assertEquals(1, sut.getSubject().getPublicCredentials(AccountName.class).size());
    assertEquals(1, sut.getSubject().getPublicCredentials(String.class).size());

    sut.setUniquePublicCredential(new AccountName("bar"));
    assertEquals("bar", sut.getSsoUsername());
    assertEquals(1, sut.getSubject().getPublicCredentials(AccountName.class).size());
    assertEquals(1, sut.getSubject().getPublicCredentials(String.class).size());
  }

  public void testSetX500PrincipalFromCertificateChain() throws Exception {
    SecurityGuard sut = new SecurityGuard();

    // When there is no chain, there should be no principal.
    sut.setX500PrincipalFromCertificateChain();
    assertNull(sut.getX500Principal());

    // When there is a chain, the principal is predictable.
    SelfSignedCertificateFactory sscf = new SelfSignedCertificateFactory();
    KeyPair keys = sscf.generateKeyPair();
    X509Certificate x509 = sscf.generateCertificate(keys,
                                                    "CN=foo",
                                                    200000);
    X509Certificate[] chain = new X509Certificate[1];
    chain[0] = x509;
    sut.setCertificateChain(chain);
    sut.setX500PrincipalFromCertificateChain();
    assertNotNull(sut.getX500Principal());
    assertEquals(1, sut.getSubject().getPrincipals().size());
  }
  
}