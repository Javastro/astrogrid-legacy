package org.astrogrid.security.myproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.SignOnClient;
import org.astrogrid.security.delegation.CertificateSigningRequest;
import org.astrogrid.security.ssl.GullibleX509TrustManager;

/**
 * A client for the Myproxy protocol.
 * Using this client, software can get credentials from a MyProxy service
 * in exchange for a user-name and password.
 *
 * @author Guy Rixon
 */
public class MyProxyClient implements SignOnClient {

  /**
   * A factory for RSA key-pairs, used in generating credentials.
   */
  private KeyPairGenerator keyGenerator;

  /**
   * A factory for reading certificates from streams.
   */
  private CertificateFactory certificateFactory;

  /**
   * The name, or the dotted-quad address, of the host for the MyProxy service.
   */
  private String serviceHost;

  /**
   * The IP port for the MyProxy service.
   */
  private int servicePort;

  /**
   * Constructs a client for a given service, using default factories.
   *
   * @param endpoint The URI for the MyProxy service.
   */
  public MyProxyClient(URI endpoint) {
    serviceHost = endpoint.getHost();
    servicePort = endpoint.getPort();

    try {
      keyGenerator = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException("Configuration failure in the MyProxy client", ex);
    }

    try {
      certificateFactory = CertificateFactory.getInstance("X509");
    } catch (CertificateException ex) {
      throw new RuntimeException("Configuration failure in the MyProxy client", ex);
    }
  }

  /** Performs the MyProxy-get operation, opening a connection to the service.
   *
   * @param userName The user whose credentials are fetched.
   * @param password The password protecting the credentials in the service.
   * @param lifetime The requested lifetime of the credentials, in seconds.
   * @param guard The object to hold the credentials.
   * @throws java.io.IOException If the service does not complete the exchange.
   * @throws java.security.GeneralSecurityException If the service rejects the command.
   */
  public void authenticate(String        userName,
                           String        password,
                           int           lifetime,
                           SecurityGuard guard) throws IOException,
                                                       GeneralSecurityException {

    // For a MyProxy-GET command, TLS-with-client-certificate authentication is
    // optional and we do not use it. Therefore, pass null for the key managers
    // to indicate that we aren't using a client key.
    SSLSocketFactory f = getSocketFactory(null);

    SSLSocket s = (SSLSocket) f.createSocket(serviceHost, servicePort);
    s.setEnabledProtocols(new String[]{"SSLv3"});
    s.setUseClientMode(true);
    s.startHandshake();
    try {
      get(userName,
          password,
          lifetime,
          guard,
          s.getOutputStream(),
          s.getInputStream());
    }
    finally {
      s.close();
    }

  }

  /**
   * In principal, determines the location of the user's home-space in VOSpace.
   * In practice, this cannot be done using Myproxy, so this method does nothing
   * and returns silently.
   *
   * @param userName The identity for which the home space is requested.
   * @param guard The object to receive the information.
   */
    public void home(String        userName,
                     SecurityGuard guard) {
      // Nothing to do here.
    }

  /**
   * Changes the user's password.
   * The request is authenticated by the old password.
   *
   * @param userName The user name known to the community.
   * @param oldPassword The old password known to the community.
   * @param newPassword The replacement password.
   */
  public void changePassword(String        userName,
                             String        oldPassword,
                             String        newPassword,
                             SecurityGuard guard) throws GeneralSecurityException,
                                                         IOException {

    // For a MyProxy-change-password command, TLS-with-client-certificate
    // authentication is mandatory. Therefore, pass the given security-guard
    // as the key manager such that the user's credentials are used in the
    // SSL handshake.
    SSLSocketFactory f = getSocketFactory(new KeyManager[]{guard});

    SSLSocket s = (SSLSocket) f.createSocket(serviceHost, servicePort);
    s.setEnabledProtocols(new String[]{"SSLv3"});
    s.setUseClientMode(true);
    s.startHandshake();
    try {
      setPassword(userName,
                  oldPassword,
                  newPassword,
                  guard,
                  s.getOutputStream(),
                  s.getInputStream());
    }
    finally {
      s.close();
    }

  }


  /**
   * Performs the MyProxy-get operation, using given streams.
   *
   * @param userName The user whose credentials are fetched.
   * @param password The password protecting the credentials in the service.
   * @param lifetime The requested lifetime of the credentials, in seconds.
   * @param guard The object to hold the credentials.
   * @param o The stream for writing to the service.
   * @param i The stream for reading from the service.
   * @throws java.io.IOException If the service does not complete the exchange.
   * @throws java.security.GeneralSecurityException If the service rejects the command.
   */
  protected void get(String        userName,
                     String        password,
                     int           lifetime,
                     SecurityGuard guard,
                     OutputStream  o,
                     InputStream   i) throws IOException,
                                             GeneralSecurityException {

    MyProxyOutputStream out = new MyProxyOutputStream(o);
    MyProxyInputStream  in  = new MyProxyInputStream(i);

    // Send the GET command to the service.
    out.startCommand();
    out.write("VERSION=MYPROXYv2");
    out.endLine();
    out.write("COMMAND=0");
    out.endLine();
    out.write("USERNAME=");
    out.write(userName);
    out.endLine();
    out.write("PASSPHRASE=");
    out.write(password);
    out.endLine();
    out.write("LIFETIME=");
    out.write(Integer.toString(lifetime));
    out.endCommand();
    
    // Exceptions are thrown here unless the service accepted the command.
    in.checkCommandStatus();
    System.out.println("Command is accepted, now sending CSR.");

    // Make the key-pair and send the public key to the service.
    KeyPair keys = keyGenerator.generateKeyPair();
    CertificateSigningRequest csr =
      new CertificateSigningRequest("CN=irrelevant", keys);
    out.write(csr.getEncoded());
    out.endCommand();

    // Read in the certificate chain. This is preceeded by one byte
    // stating the number of certificates.
    int nCertificates = in.read();
    System.out.println("Number of certificates: " + nCertificates);
    List<X509Certificate> l = new ArrayList<X509Certificate>(nCertificates);
    for (int c = 0; c < nCertificates; c++) {
      l.add((X509Certificate)certificateFactory.generateCertificate(in));
    }

    // Package the credentials for return to the caller.
    CertPath path = certificateFactory.generateCertPath(l);
    guard.setCertificateChain(path);
    guard.setPrivateKey(keys.getPrivate());

    // Read the response to the certificate exchange.
    in.checkCommandStatus();
  }

  /**
   * Performs the MyProxy-change-password operation, using given streams.
   * The security-guard object passed as a parameter must contain credentials
   * for the given account sufficent to authenticate the account name to the
   * MyProxy service using TLS-with-client-certificate (proxy credentials are
   * acceptable).
   *
   * @param userName The user whose password is changed.
   * @param oldPassword The current password.
   * @param newPassword The desired password.
   * @param guard The object holding the credentials.
   * @param o The stream for writing to the service.
   * @param i The stream for reading from the service.
   * @throws java.io.IOException If the service does not complete the exchange.
   * @throws java.security.GeneralSecurityException If the service rejects the command.
   */
  protected void setPassword(String        userName,
                             String        oldPassword,
                             String        newPassword,
                             SecurityGuard guard,
                             OutputStream  o,
                             InputStream   i) throws IOException,
                                                     GeneralSecurityException {
    assert o != null;
    assert i != null;

    MyProxyOutputStream out = new MyProxyOutputStream(o);
    MyProxyInputStream  in  = new MyProxyInputStream(i);

    // Send the GET command to the service.
    out.startCommand();
    out.write("VERSION=MYPROXYv2");
    out.endLine();
    out.write("COMMAND=4");
    out.endLine();
    out.write("USERNAME=");
    out.write(userName);
    out.endLine();
    out.write("PASSPHRASE=");
    out.write(oldPassword);
    out.endLine();
    out.write("NEW_PHRASE=");
    out.write(newPassword);
    out.write("LIFETIME=0");
    out.endCommand();

    // Exceptions are thrown here unless the service accepted the command.
    in.checkCommandStatus();
  }

  /**
   * Creates a socket factory suitable for MyProxy communications.
   * The factory uses a {@link GullibleX509Trustmanager}; this means that
   * connections via hte factory perform a weak authentication of the server
   * without checking trust anchors. If key managers (typically, instances of
   * {@link SecurityGuard}) are passed, these are used to set the caller's
   * credentials; otherwise, no client credentials are passed to the server.
   *
   * @param kms The key managers; pass a null array to use defaults.
   * @return The factory.
   */
  protected SSLSocketFactory getSocketFactory(KeyManager[] kms) {
    try {
      SSLContext ssl = SSLContext.getInstance("SSLv3");
      TrustManager[] tms = {new GullibleX509TrustManager()};

      ssl.init(kms, tms, null);
      return ssl.getSocketFactory();
    }
    catch (Exception e) {
      // This cannot happen in service unless there is a bug.
      throw new RuntimeException("Failed to configure SSLv3", e);
    }
  }

}