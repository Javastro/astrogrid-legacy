/*
 * SsoClientTestCase.java
 * JUnit based test
 *
 * Created on February 12, 2008, 9:45 AM
 */

package org.astrogrid.community.resolver;

import java.security.cert.X509Certificate;
import junit.framework.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.Subject;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;

/**
 * JUnit tests for SSOClient.
 *
 * @author Guy Rixon
 */
public class SsoClientTestCase extends TestCase {
  
  public SsoClientTestCase(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
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
