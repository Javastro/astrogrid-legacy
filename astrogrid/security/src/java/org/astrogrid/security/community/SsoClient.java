/*
 * Copyright (c) 2004-2008 AstroGrid. All rights reserved.
 */

package org.astrogrid.security.community;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.security.auth.Subject;
import org.astrogrid.security.HomespaceLocation;
import org.bouncycastle.openssl.PEMWriter;

/**
 * A client for the SSO service in the AstroGrid community product.
 * <p>
 * The service generates and returns a certificate chain based on credentials
 * stored in the community and on a public key sent with the request. This
 * client generates a key pair, posts the request, and returns the 
 * certificates plus the private key in a JASS subject.
 *
 * @author Guy Rixon
 */
public class SsoClient extends Mockery {
  
  /**
   * The URI for the SSO or "accounts" service.
   * This is the registered endpoint and has to be extended to give the
   * URI for the credentials for a particular user.
   */
  private String endpoint;
  
  /**
   * Constructs a SsoClient.
   *
   * @param endpoint The endpoint URI for the service.
   */
  public SsoClient(String endpoint) {
    this.endpoint = endpoint;
  }
  
  /**
   * Signs a user into the IVO. Provides a certificate chain and private key
   * suitable for authenticating to services; these are written into the given
   * Subject. If the property org.astrogrid.security.mock.community is set, 
   * then this is a unit test and fake credential are written.
   *
   * @param userName The user-name as known to the community service.
   * @param password The password, unhashed and unencrypted.
   * @param lifetime The duration of validity of the credentials, in seconds.
   * @param subject The Subject to receive the credentials.
   * @throws IOException If communication with the service fails.
   * @throws CertificateException If the service returns invalid credentials.
   */
  public void authenticate(String  userName, 
                           String  password, 
                           int     lifetime,
                           Subject subject) throws IOException,
                                                   CertificateException {
    assert userName != null;
    assert password != null;
    assert lifetime > 0;
    assert subject != null;
    
    // If this is a test, load no credentials.
    // @TODO: load some fake credentials.
    if (isMock()) {
      return;
    }
  
    // Derive the URL from which to read the give user's credentials.
    URL proxyUrl = getProxyUrl(userName);
      
    // Generate a key pair. The private key stays here and the public key
    // eventually goes with the credential request to the community.
    KeyPair keys = generateKeyPair();
      
    // Open a connection to the web resource.
    HttpURLConnection connection =  (HttpURLConnection) (proxyUrl.openConnection());
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setRequestMethod("POST");
      
    // Set up HTTPS if necessary. This makes the connection accept
    // any server identified by X509 credentials, regardless of the
    // content of those credentials.
    if (connection instanceof HttpsURLConnection) {
      configureHttps((HttpsURLConnection) connection);
    }
      
    // Send the password and the public key and the lifetime as parameters.
    // The keys is formatted as a PEM object.
    String passwordParameter = URLEncoder.encode(password, "UTF-8");
    StringWriter sw = new StringWriter();
    PEMWriter pw = new PEMWriter(sw);
    pw.writeObject(keys.getPublic());
    pw.close();
    String keyParameter = URLEncoder.encode(sw.toString(), "UTF-8");
    String lifetimeParameter = 
        URLEncoder.encode(Integer.toString(lifetime), "UTF-8");
    OutputStream os = connection.getOutputStream();
    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
    osw.write("password=");
    osw.write(passwordParameter);
    osw.write("&key=");
    osw.write(keyParameter);
    osw.write("&lifetime=");
    osw.write(lifetimeParameter);
    osw.flush();
    osw.close();
      
    // Read the certificate chain from the HTTP response.
    // This throws CertificateException if the response is invalid and
    // IOException if the response cannot be read at all. 
    CertPath path = readCertificates(connection.getInputStream());
      
    // Pack and return the credentials.
    subject.getPrivateCredentials().add(keys.getPrivate());
    subject.getPublicCredentials().add(path);
  }
  
  /**
   * Reads the location of a user's home space from the accounts service.
   * Records the location as a principal in the given subject.
   *
   * @param userName The user-name as known to the community service.
   * @param subject The Subject to receive the credentials.
   * @throws IOException If communication with the service fails.
   */
  public void home(String  userName, 
                   Subject subject) throws IOException {
    assert userName != null;
    assert subject != null;
    
    // If this is a test, load no credentials.
    // @TODO: load some fake credentials.
    if (isMock()) {
      return;
    }
  
    // Derive the URL from which to read the give user's homespace.
    URL homeUrl = getHomeUrl(userName);
      
    // Open and configure a connection to the web resource.
    // Don't follow any HTTP redirections: the target of such a redirection
    // is the answer we want to extract.
    HttpURLConnection connection =  (HttpURLConnection) (homeUrl.openConnection());
    connection.setDoOutput(false);
    connection.setDoInput(true);
    connection.setRequestMethod("GET");
    connection.setFollowRedirects(false);
      
    // Set up HTTPS if necessary. This makes the connection accept
    // any server identified by X509 credentials, regardless of the
    // content of those credentials.
    if (connection instanceof HttpsURLConnection) {
      configureHttps((HttpsURLConnection) connection);
    }
    
    // There are no query-parameters for this request.
    
    // The reply should be a redirection to the homespace URI.
    // Extract that URI.
    String homespace = null;
    if (connection.getResponseCode() == connection.HTTP_SEE_OTHER  ||
        connection.getResponseCode() == connection.HTTP_MOVED_PERM ||
        connection.getResponseCode() == connection.HTTP_MOVED_TEMP) {
      homespace = connection.getHeaderField("Location");
    }
    
    // If the homespace was read, record it as a principal.
    // If not, assume that the account has no homespace; therefore this
    // is not an error.
    if (homespace != null) {
      subject.getPrincipals().add(new HomespaceLocation(homespace));
    }
  }
  
  /**
   * Reads a certificate chain from a stream.
   */
  protected CertPath readCertificates(InputStream is) 
     throws CertificateException, IOException {
    CertificateFactory factory = CertificateFactory.getInstance("X509");
    return factory.generateCertPath(is, "PkiPath");
  }
  
  /**
   * Generates the URL for the web resource holding the user's proxy.
   */
  private URL getProxyUrl(String userName) {
    try {
      return new URL(endpoint + "/" + userName + "/proxy");
    }
    catch (Exception e) {
      // This cannot happen in service unless there is a bug.
      throw new RuntimeException("Failed to construct a URL for a user proxy", e);
    }
  }
  
  /**
   * Generates the URL for the web resource holding the user's homespace.
   */
  private URL getHomeUrl(String userName) {
    try {
      return new URL(endpoint + "/" + userName + "/home");
    }
    catch (Exception e) {
      // This cannot happen in service unless there is a bug.
      throw new RuntimeException("Failed to construct a URL for a user homespace", e);
    }
  }
  
  /**
   * Configures an HTTPS connection to the community. TLS is specified as the
   * encryption protocol. Neither the client nor the server is authenticated; 
   * the TLS is used for privacy, not identification.
   */
  private void configureHttps(HttpsURLConnection connection) {
    assert connection != null;
    try {
      SSLContext ssl = SSLContext.getInstance("TLS");
      TrustManager[] tms = {new GullibleX509TrustManager()};
      ssl.init(null, tms, null);
      connection.setSSLSocketFactory(ssl.getSocketFactory());
    }
    catch (Exception e) {
      // This cannot happen in service unless there is a bug.
      throw new RuntimeException("Failed to configure HTTPS", e);
    }
  }
  
  /**
   * Generates a key-pair to use in the proxy certificate. 
   * The keys are RSA keys.
   *
   * @return The key-pair.
   */
  private KeyPair generateKeyPair() {
    try {
      return KeyPairGenerator.getInstance("RSA").generateKeyPair();
    } catch (Exception e) {
      // This cannot happen in service unless there is a bug.
      throw new RuntimeException("Failed to generate a key pair", e);
    }
  }
  
}
