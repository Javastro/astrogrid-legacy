package org.astrogrid.security.rfc3820;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;

/**
 * JUnit 3 tests for ProxyCertificateFactory.
 *
 * @author Guy Rixon
 */
public class ProxyCertificateFactoryTest extends TestCase {
  
  private KeyPair caKeys;
  private X509Certificate caCertificate;
  private SelfSignedCertificateFactory factory;
  
  public void setUp() throws Exception {
    this.factory = new SelfSignedCertificateFactory();
    this.caKeys  = this.factory.generateKeyPair();
    this.caCertificate = this.factory.generateCertificate(
        this.caKeys,
        "O=AstroGrid,OU=test,OU=community,CN=ca",
        720000
    );
  }
  
  public void testCreateProxyCertificate() throws Exception {
    
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.myproxy", 
                                            "/dev/null");
    System.out.println("Subject: " + this.caCertificate.getSubjectX500Principal());
    System.out.println("Issuer:  " + this.caCertificate.getIssuerX500Principal());
    
    
    // Generate an EEC from the CA fixture.
    KeyPair entityKeys = this.factory.generateKeyPair();
    X509Certificate eec = this.factory.generateCertificate(
        this.caKeys,
        entityKeys,
        this.caCertificate,
        "O=AstroGrid,OU=test,OU=community,CN=foo",
        480000
    );
    System.out.println("Subject: " + eec.getSubjectX500Principal());
    System.out.println("Issuer:  " + eec.getIssuerX500Principal());
    
    // Generate a proxy based on the above EEC using the SUT.
    KeyPair proxyKeys = this.factory.generateKeyPair();
    ProxyCertificateFactory sut = new ProxyCertificateFactory();
    X509Certificate proxy = sut.createProxyCertificate(eec, 
                                                       entityKeys.getPrivate(),
                                                       proxyKeys.getPublic(),
                                                       360000, 
                                                       false);
    assertNotNull(proxy);
    System.out.println("Subject: " + proxy.getSubjectX500Principal());
    System.out.println("Issuer:  " + proxy.getIssuerX500Principal());
    assertEquals(proxy.getIssuerX500Principal(), 
                 eec.getSubjectX500Principal());
    
    X509Certificate[] chain  = {proxy, eec};
    X509Certificate[] anchor = {this.caCertificate};
    
    CertificateChainValidator v = new CertificateChainValidator(anchor);
    v.validate(chain);
  }
  
  public void testExtendCertificateChain() throws Exception {
    
    // Generate an EEC from the CA fixture.
    KeyPair entityKeys = this.factory.generateKeyPair();
    X509Certificate eec = this.factory.generateCertificate(
        this.caKeys,
        entityKeys,
        this.caCertificate,
        "O=AstroGrid,OU=test,OU=community,CN=foo",
        480000
    );
    List<X509Certificate> chain = new ArrayList<X509Certificate>(2);
    chain.add(eec);
    assertEquals(1, chain.size());
    
    KeyPair proxyKeys = this.factory.generateKeyPair();
    
    ProxyCertificateFactory sut = new ProxyCertificateFactory();
    sut.extendCertificateChain(chain,
                               entityKeys.getPrivate(),
                               proxyKeys.getPublic(),
                               48000,
                               false);
    assertEquals(2, chain.size());
    
    X509Certificate[] anchors = {this.caCertificate};
    CertificateChainValidator v = new CertificateChainValidator(anchors);
    v.validate(chain);
  }
  
}
