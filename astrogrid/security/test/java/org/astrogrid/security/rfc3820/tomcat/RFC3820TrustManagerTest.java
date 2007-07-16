package org.astrogrid.security.rfc3820.tomcat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import junit.framework.TestCase;

/**
 * JUnit tests for RFC3820TrustManager.
 *
 * @author Guy Rixon
 */
public class RFC3820TrustManagerTest extends TestCase {
  
  public void testGoodChain() throws Exception {
    RFC3820TrustManager sut 
        = new RFC3820TrustManager(this.loadAnchors());
    sut.checkClientTrusted(this.loadClientChain(), "foo");
  }
  
  private X509Certificate[] loadClientChain() throws Exception {
    KeyStore store = KeyStore.getInstance("JKS");
    InputStream is = this.getClass().getResourceAsStream("/tester.jks");
    assertNotNull(is);
    store.load(is, "testing".toCharArray());
    is.close();
    Certificate[] chain1 = store.getCertificateChain("tester");
    X509Certificate[] chain2 = new X509Certificate[chain1.length];
    for (int i = 0; i < chain2.length; i++) {
      chain2[i] = (X509Certificate)chain1[i];
    }
    return chain2;
  }
  
  private X509Certificate[] loadAnchors() throws Exception {
    CertificateFactory factory = CertificateFactory.getInstance("X509");
    X509Certificate[] anchors = new X509Certificate[1];
    File anchorFile = new File("pem", "unit-test-ca.0");
    InputStream is = new FileInputStream(anchorFile);
    anchors[0] = (X509Certificate)(factory.generateCertificate(is));
    return anchors;
  }
  
}
