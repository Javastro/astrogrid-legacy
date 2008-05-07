package org.astrogrid.security.community;

import java.security.cert.X509Certificate;
import java.io.InputStream;
import java.security.cert.CertPath;
import javax.security.auth.Subject;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;

/**
 * JUnit tests for SSOClient.
 *
 * @author Guy Rixon
 */
public class SsoClientTest extends TestCase {
  
  public SsoClientTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    SimpleConfig.getSingleton().setProperty(
      "org.astrogrid.security.community.SsoClient.mock",
      "true"
    );
  }

  protected void tearDown() throws Exception {
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
