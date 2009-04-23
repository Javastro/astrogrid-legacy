package org.astrogrid.security;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import junit.framework.TestCase;
import org.apache.axis.client.Call;
//import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.security.sample.SampleDelegate;


/**
 * JUnit tests for end-to-end operation of the security facade in
 * a simulated web-service. Axis' "loop-back" mode is used, in
 * which all parts of the Axis handler-chain are exercise, but there
 * is no HTTP communication and hence no need to deploy the SUT
 * into a web-container.
 *
 * @author Guy Rixon
 */
public class SoapSignatureEndToEndTest extends TestCase {
  
  @Override
  public void setUp() throws Exception {
    TestUtilities.copyResourceToFile(this.getClass(),
                                     "/server-config.wsdd",
                                     "server-config.wsdd");
  }

  public void testGoodCredentials() throws Exception {

    // Make the URI scheme 'local' known to java.net.URL.
    Call.initialize();
    
    // Obtain credentials and write to disc.
    // In routine testing, the credentials are already on disc,
    // since a copy is kept in CVS. This call is only needed when the
    // credentials are being replaced.
    //this.storeCredentials();
    
    // Load the credentials from disc.
    SecurityGuard g2 = this.loadCredentials();
    
    // Make a delegate using the local transport. The service
    // name SamplePort matches the name declared in
    // server-config.wsdd.
    URL endpoint = new URL("local:///SamplePort");
    SampleDelegate delegate = new SampleDelegate(endpoint);
    delegate.setCredentials(g2);

    // Exercise the delegate, the handler chains and the service
    // implementation.  This simulates a call to a real web-service.
    String name = delegate.whoAmI();
    System.out.println("Client: returned name: " + name);
  }


  public void testMissingCredentials() throws Exception {

    // Make the URI scheme 'local' known to java.net.URL.
    Call.initialize();

    // Make the guard without any credentials.
    SecurityGuard g = new SecurityGuard();

    // Make a delegate using the local transport. The service
    // name SamplePort matches the name declared in
    // server-config.wsdd.
    URL endpoint = new URL("local:///SamplePort");
    SampleDelegate delegate = new SampleDelegate(endpoint);
    String name = delegate.whoAmI();
    System.out.println("Client: returned name: " + name);
    assertEquals(name, "anonymous");
  }
  
  public SecurityGuard loadCredentials() throws Exception {
    File pem = new File("pem");
    System.out.println("Looking for trust anchors as PEM files in "
                       + pem.getAbsolutePath());
    System.setProperty("X509_CERT_DIR", pem.getAbsolutePath());
    
    SecurityGuard guard = new SecurityGuard();
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
    guard.setCertificateChain(chain2);
    PrivateKey key = (PrivateKey)store.getKey("tester", "testing".toCharArray());
    guard.setPrivateKey(key);
    return guard;
  }
  
  
  /*
   * This method is only useful when you need to replace the credentials
   * used for the test. Normally, the test should use the credentials stored
   * in CVS. Enabling this method causes a dependency on the client, common
   * and resolver jars of the community component.
   *
  public void storeCredentials() throws Exception {
    // Configure the registry-query endpoint; the password resolver
    // needs this. Use the main, production registry in order to see the
    // right community. 
    URL registryEndpoint
        = new URL("http://galahad.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery");
    SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.endpoint", 
                                            registryEndpoint);
    // Log in at the community and receive credentials.
    System.out.println("Logging in...");
    CommunityPasswordResolver resolver
        = new CommunityPasswordResolver();
    String account = "ivo://astrogrid.cam/tester";
    String password = "testing";
    int durationOfValidity = 3600 * 24 * 365 * 10; // Ten years
    String trustedCertsDirectory
        = "/C:/Documents and Settings/gtr/.workbench/trusted-certificates";
    Subject s = resolver.checkPassword(account, 
                                       password,
                                       durationOfValidity,
                                       trustedCertsDirectory);
    SecurityGuard g1 = new SecurityGuard(s);
   
    // Store the credentials to disc.
    PrivateKey key = g1.getPrivateKey();
    X509Certificate[] chain = g1.getCertificateChain();
    KeyStore keystore1 = KeyStore.getInstance("JKS");
    keystore1.load(null, null);
    keystore1.setKeyEntry("tester", key, "testing".toCharArray(), chain);
    
    File f = new File("tester.jks");
    f.createNewFile();
    assert f.exists();
    FileOutputStream fos = new FileOutputStream(f);
    keystore1.store(fos, "testing".toCharArray());
    fos.close();
  }
  */
}