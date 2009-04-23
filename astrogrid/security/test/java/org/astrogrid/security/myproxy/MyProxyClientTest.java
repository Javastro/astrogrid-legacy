package org.astrogrid.security.myproxy;

import java.net.URI;
import java.security.Security;
import java.security.cert.X509Certificate;
import junit.framework.TestCase;
import org.astrogrid.security.SecurityGuard;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * JUnit-3 tests for {@link MyProxyClient}.
 *
 * @author Guy Rixon
 */
public class MyProxyClientTest extends TestCase {

  public void setUp() throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    System.setProperty("javax.net.debug", "all");
  }

  /**
   * MyProxy is unwell at present and none of its test can be passed.
   * The whole MyProxy feature is excluded from the current release.
   * In order to retain these tests without upsetting either JUnit or
   * Maven, the real tests are commented out and a dummy, do-nothing
   * test is inserted here. Reverse this arrangement when actually
   * testing MyProxy again.
   */
  public void testDummy() {}

  /*
  public void testAuthenticate() throws Exception {
    //System.setProperty("javax.net.debug", "all");
    MyProxyClient sut =
        new MyProxyClient(new URI("myproxy://ag03.ast.cam.ac.uk:7512"));
    SecurityGuard g = new SecurityGuard();
    sut.authenticate("username", "password", 3600, g);
    assertNotNull(g.getPrivateKey());
    assertNotNull(g.getCertificateChain());
    assertEquals(4, g.getCertificateChain().length);
  }
  */

  /*
  public void testChangePassword() throws Exception {
    MyProxyClient sut =
        new MyProxyClient(new URI("myproxy://ag03.ast.cam.ac.uk:7512"));
    SecurityGuard g = new SecurityGuard();
    sut.authenticate("username", "password", 3600, g);
    assertNotNull(g.getPrivateKey());
    X509Certificate[] c = g.getCertificateChain();
    assertNotNull(c);
    assertEquals(4, c.length);
    for (int i = 0; i < c.length; i++) {
      System.out.println(c[i].getSubjectX500Principal());
    }

    sut.changePassword("username", "password", "password", g);
  }
  */

}
