package org.astrogrid.community.resolver;

import java.io.BufferedInputStream;
import java.io.EOFException;
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
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.security.auth.Subject;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
public class SsoClient {
  
  /**
   * The URI for the SSO service.
   * This is the registered endpoint and has to be extended to give the
   * URI for the credentials for a particular user.
   */
  private String endpoint;
  
  /**
   * Constructs a SsoClient.
   */
  public SsoClient(String endpoint) {
    this.endpoint = endpoint;
  }
  
  public Subject authenticate(String userName, 
                              String password, 
                              int    lifetime,
                              String trustAnchors) 
      throws CommunityResolverException {

    try {
      
      // Derive the URL for the user's proxy.
      URL proxyUrl = new URL(endpoint + "/" + userName + "/proxy");
      
      // Generate a key pair. The private key stays here and the public key
      // eventually goes with the credential request to the community.
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      KeyPair keys = generator.generateKeyPair();
      
      // Open a connection to the proxy resource.
      HttpURLConnection connection = 
          (HttpURLConnection) (proxyUrl.openConnection());
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setRequestMethod("POST");
      
      // Set up HTTPS if necessary. This makes the connection accept
      // any server identified by X509 credentials, regardless of the
      // content of those credentials.
      if (connection instanceof HttpsURLConnection) {
        HttpsURLConnection https = (HttpsURLConnection) connection;
        SSLContext ssl = SSLContext.getInstance("TLS");
        TrustManager[] tms = {new GullibleX509TrustManager()};
        ssl.init(null, tms, null);
        https.setSSLSocketFactory(ssl.getSocketFactory());
      }
      
      // Send the password and the public key and the lifetime as parameters.
      String passwordParameter = URLEncoder.encode(password, "UTF-8");
      StringWriter sw = new StringWriter();
      PEMWriter pw = new PEMWriter(sw);
      pw.writeObject(keys.getPublic());
      pw.close();
      String keyParameter = URLEncoder.encode(sw.toString(), "UTF-8");
      String lifetimeParameter = 
          URLEncoder.encode(new Integer(lifetime).toString(), "UTF-8");
      OutputStreamWriter osw = 
          new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
      osw.write("password=");
      osw.write(passwordParameter);
      osw.write("&key=");
      osw.write(keyParameter);
      osw.write("&lifetime=");
      osw.write(lifetimeParameter);
      osw.flush();
      osw.close();
      
      // Read the representation returned by the connection.
      CertPath path = readCertificates(connection.getInputStream());
      
      // Pack and return the credentials.
      Subject subject = new Subject();
      subject.getPrivateCredentials().add(keys.getPrivate());
      subject.getPublicCredentials().add(path);
      return subject;
    }
    catch (Exception e) {
      throw new CommunityResolverException("Failed to get credentials", e);
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
  
}
