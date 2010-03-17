package org.astrogrid.security.rfc3820.tomcat6;

import java.io.File;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit-4 tests for RFC3820TrustManager.
 *
 * @author Guy Rixon
 */
public class RFC3820TrustManagerTest {

  @Test
  public void testGoodChain() throws Exception {
    RFC3820TrustManager sut 
        = new RFC3820TrustManager(this.loadAnchors());

    // Check that it accepts an EEC signed by the given CA.
    sut.checkClientTrusted(this.loadClientChain(), "foo");


    // Check that it doesn't forget the anchors. See BZ2991.
    assertArrayEquals(loadAnchors(), sut.getAcceptedIssuers());
  }

  @Test(expected=CertificateException.class)
  public void testCheckServerTrusted() throws Exception {
    RFC3820TrustManager sut
        = new RFC3820TrustManager(this.loadAnchors());
    sut.checkServerTrusted(loadClientChain(), null);
  }

  
  private X509Certificate[] loadClientChain() throws Exception {
    KeyStore store = KeyStore.getInstance("JKS");
    InputStream is = this.getClass().getResourceAsStream("/tester.jks");
    assertNotNull(is);
    store.load(is, "testing".toCharArray());
    is.close();
    Certificate[] chain1 = store.getCertificateChain("tester");
    X509Certificate x = (X509Certificate) chain1[chain1.length-1];
    int nToCopy = chain1.length;
    X509Certificate[] chain2 = new X509Certificate[nToCopy];
    for (int i = 0; i < nToCopy; i++) {
      chain2[i] = (X509Certificate) chain1[i];
    }
    return chain2;
  }
  
  private X509Certificate[] loadAnchors() throws Exception {
    CertificateFactory factory = CertificateFactory.getInstance("X509");
    X509Certificate[] anchors = new X509Certificate[1];
    File anchorFile = new File("target/pem", "unit-test-ca.0");
    InputStream is = this.getClass().getResourceAsStream("/unit-test-ca.0");
    anchors[0] = (X509Certificate)(factory.generateCertificate(is));
    return anchors;
  }
  
}
