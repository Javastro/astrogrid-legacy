package org.astrogrid.security.community;

import java.security.cert.X509Certificate;
import java.io.InputStream;
import java.security.cert.CertPath;
import junit.framework.TestCase;
import org.astrogrid.community.server.sso.AccountServlet;
import org.astrogrid.community.server.sso.PondLifeDb;
import org.astrogrid.security.SecurityGuard;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * JUnit tests for SSOClient.
 *
 * @author Guy Rixon
 */
public class SsoClientTest extends TestCase {
  
  private Server jetty;

  protected void setUp() throws Exception {
    
    // Pre-load the community database with a test account.
    PondLifeDb pond = new PondLifeDb();
    pond.initialize();
    
    // Run the community service in an embedded copy of Jetty.
    jetty = new Server(6666);
    Context c = new Context(jetty,"/community",Context.SESSIONS);
    c.addServlet(new ServletHolder(new AccountServlet()), "/accounts/*");
    jetty.start();
  }
  
  protected void tearDown() throws Exception {
    jetty.stop();
  }

  public void testChangePassword() throws Exception {
    SsoClient sut = new SsoClient("http://localhost:6666/community/accounts");
    SecurityGuard g = new SecurityGuard();
    sut.changePassword("frog", "croakcroak", "ribbitribbit", g);
    sut.authenticate("frog", "ribbitribbit", 3600, g);
  }
  
  public void testHome() throws Exception {
    SsoClient sut = new SsoClient("http://localhost:6666/community/accounts");
    SecurityGuard g = new SecurityGuard();
    sut.home("frog", g);
  }
  
  public void testAuthenticate() throws Exception {
    SsoClient sut = new SsoClient("http://localhost:6666/community/accounts");
    sut.authenticate("frog", "croakcroak", 3600, new SecurityGuard());
  }

  /**
   * Test of readCertificates method, of class org.astrogrid.community.resolver.SsoClient.
   */
  public void testReadGoodCertificates() throws Exception {
    System.out.println("readGoodCertificates");
    
    // The URL doesn't matter here since the test does not trigger
    // a request to the service.
    SsoClient sut = new SsoClient("http://foo.bar/baz");
    
    // This is a known, good certificate-path, supplied as a resource file
    // for the tests. It was derived from a working installation of the
    // accounts service.
    InputStream is = this.getClass().getResourceAsStream("/gtr.pkipath");
    CertPath result = sut.readCertificates(is);
    assertNotNull(result);
    assertEquals(2, result.getCertificates().size());
    
    X509Certificate proxy = (X509Certificate) (result.getCertificates().get(0));
    assertNotNull(proxy);
    
    X509Certificate eec = (X509Certificate) (result.getCertificates().get(1));
    assertNotNull(eec);
  }
  
}
