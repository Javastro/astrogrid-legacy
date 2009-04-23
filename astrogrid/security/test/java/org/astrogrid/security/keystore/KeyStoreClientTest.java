package org.astrogrid.security.keystore;

import java.io.File;
import java.net.URI;
import junit.framework.TestCase;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.TestUtilities;

/**
 * JUnit-3 tests for KeyStoreClient.
 *
 * @author Guy Rixon
 */
public class KeyStoreClientTest extends TestCase {
  
  private URI jksUri;
  private URI p12Uri;

  @Override
  public void setUp() throws Exception {
    File jksStore = TestUtilities.copyResourceToFile(this.getClass(), "/tester.jks");
    jksUri = jksStore.toURI();

    File p12Store = TestUtilities.copyResourceToFile(this.getClass(), "/tester.jks");
    p12Uri = p12Store.toURI();
  }

  /**
   * Test of authenticate method, of class KeyStoreClient.
   */
  public void testAuthenticateGoodPassword() throws Exception {
    System.out.println("authenticate");
    String userName = "tester";
    String password = "testing";
    int lifetime = 360000;
    SecurityGuard guard = new SecurityGuard();
    KeyStoreClient sut = new KeyStoreClient(jksUri);
    sut.authenticate(userName, password, lifetime, guard);
    assert(guard.getPrivateKey() != null);
    assert(guard.getX500Principal() != null);
    assert(guard.getCertificateChain().length > 0);
  }

  /**
   * Test of authenticate method, of class KeyStoreClient.
   */
  public void testAuthenticateBadPassword() throws Exception {
    System.out.println("authenticate");
    String userName = "tester";
    String password = "wrong";
    int lifetime = 360000;
    SecurityGuard guard = new SecurityGuard();
    KeyStoreClient sut = new KeyStoreClient(jksUri);
    try {
      sut.authenticate(userName, password, lifetime, guard);
      fail("Expected an exception from a failed authentication.");
    }
    catch (Exception e) {
      assertNull(guard.getX500Principal());
    }
  }

  /**
   * Test of authenticate method, of class KeyStoreClient.
   */
  public void testAuthenticateP12() throws Exception {
    System.out.println("authenticate");
    String userName = "tester";
    String password = "testing";
    int lifetime = 360000;
    SecurityGuard guard = new SecurityGuard();
    KeyStoreClient sut = new KeyStoreClient(p12Uri);
    sut.authenticate(userName, password, lifetime, guard);
    assert(guard.getPrivateKey() != null);
    assert(guard.getX500Principal() != null);
    assert(guard.getCertificateChain().length > 0);
  }

  /**
   * Test of home method, of class KeyStoreClient.
   */
  public void testHome() throws Exception {
    System.out.println("home");
    String userName = "tester";
    SecurityGuard guard = new SecurityGuard();
    KeyStoreClient sut = new KeyStoreClient(jksUri);

    sut.home(userName, guard);
    
    // The key-store doesn't know the home-space and the SUT shouldn't
    // make one up.
    assertNull(guard.getHomespaceLocationAsString());
  }

  /**
   * Test of changePassword method, of class KeyStoreClient.
   */
  public void testChangePasswordJks() throws Exception {
    System.out.println("changePassword");
    String userName = "tester";
    String oldPassword = "testing";
    String newPassword = "different";
    SecurityGuard guard = new SecurityGuard();

    KeyStoreClient sut = new KeyStoreClient(jksUri);
    sut.changePassword(userName, oldPassword, newPassword, guard);

    sut.authenticate(userName, newPassword, 360000, guard);
  }

  /**
   * Test of changePassword method, of class KeyStoreClient.
   */
  public void testChangePasswordP12() throws Exception {
    System.out.println("changePassword");
    String userName = "tester";
    String oldPassword = "testing";
    String newPassword = "different";
    SecurityGuard guard = new SecurityGuard();

    KeyStoreClient sut = new KeyStoreClient(p12Uri);
    sut.changePassword(userName, oldPassword, newPassword, guard);

    sut.authenticate(userName, newPassword, 360000, guard);
  }

}
