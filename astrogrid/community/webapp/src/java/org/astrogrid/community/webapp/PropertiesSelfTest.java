package org.astrogrid.community.webapp;

import java.io.File;
import java.net.URI;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.SecurityGuard;

/**
 * JUnit tests on the settings of community properties. 
 *
 * @author Guy Rixon
 */
public class PropertiesSelfTest extends TestCase {
  
  public void testCommunityIdent() throws Exception {
    System.out.println("Testing the community identifier");
    String id = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.ident");
    System.out.println(id);
    
    // It must be a valid part of an IVORN.
    URI u = new URI("ivo://" + id);
  }
  
  public void testBaseUrl() throws Exception {
    System.out.println("Testing the VOSI base URL");
    URL vosi = 
        SimpleConfig.getSingleton().getUrl("org.astrogrid.vosi.baseurl");
    System.out.println(vosi);
    
    // Check that it's HTTP.
    assertEquals("URL scheme for org.astrogrid.vosi.baseurl should be http",
                 "http", vosi.getProtocol());
    
    // Check that something responds
    vosi.getContent();
  }
  
  public void testBaseUrlSecure() throws Exception {
    System.out.println("Testing the VOSI secure base URL");
    URL vosi = 
        SimpleConfig.getSingleton().getUrl("org.astrogrid.vosi.baseurlsecure");
    System.out.println(vosi);
    
    // Check that it's HTTPS.
    assertEquals("https", vosi.getProtocol());
    
    // Check that something responds.
    HttpsURLConnection c = (HttpsURLConnection) (vosi.openConnection());
    SecurityGuard sg = new SecurityGuard();
    sg.configureHttps(c);
    /*
    SSLContext ssl = SSLContext.getInstance("TLS");
    TrustManager[] tms = {new GullibleX509TrustManager()};
    ssl.init(null, tms, null);
    c.setSSLSocketFactory(ssl.getSocketFactory());
    */
    c.getContent();
  }
  
  public void testVoSpaceService() throws Exception {
    System.out.println("Testing the VOSpace setting");
    String voSpaceIvorn = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.default.vospace");
    System.out.println(voSpaceIvorn);
  }
  
  public void testRegistryQuery() throws Exception {
    System.out.println("Testing registry-query endpoint");
    URL registryEndpoint = 
        SimpleConfig.getSingleton().getUrl("org.astrogrid.registry.query.endpoint");
    System.out.println(registryEndpoint);
    
    // Check that something responds. We expect the "content" of the 
    // registry's SOAP endpoint to be terse error-message.
    registryEndpoint.getContent();
  }
  
  public void testDbConfigUrl() throws Exception {
    System.out.println("Testing the database-configuration URL");
    URL db = 
        SimpleConfig.getSingleton().getUrl("org.astrogrid.community.dbconfigurl");
    System.out.println(db);
    
    // Check that something responds
    db.getContent();
  }
  
  public void testCredentialLocation() throws Exception {
    System.out.println("Testing the credential location");
    String location = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.myproxy");
    System.out.println(location);
    
    File f = new File(location);
    assertTrue("Credentials directory does not exist.",    f.exists());
    assertTrue("Credentials directory is not a directory", f.isDirectory());
  }
  
  public void testCaKey() throws Exception {
    System.out.println("Testing the CA key");
    String location = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.cakey");
    System.out.println(location);
    
    File f = new File(location);
    assertTrue("CA key does not exist.",       f.exists());
    assertTrue("CA key is a not a plain file", f.isFile());
    assertTrue("CA key cannot be read.",       f.canRead());
  }
  
  public void testCaCertificate() throws Exception {
    System.out.println("Testing the CA certificate");
    String location = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.cacert");
    System.out.println(location);
    
    File f = new File(location);
    assertTrue("CA certificate does not exist.",       f.exists());
    assertTrue("CA certificate is a not a plain file", f.isFile());
    assertTrue("CA certificate cannot be read.",       f.canRead());
  }
  
  public void testCaSerial() throws Exception {
    System.out.println("Testing the CA serial-number file");
    String location = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.caserial");
    System.out.println(location);
    
    File f = new File(location);
    assertTrue("CA serial file does not exist.",       f.exists());
    assertTrue("CA serial file is a not a plain file", f.isFile());
    assertTrue("CA serial file cannot be read.",       f.canRead());
    assertTrue("CA serial file cannot be written.",    f.canWrite());
  }
  
}
