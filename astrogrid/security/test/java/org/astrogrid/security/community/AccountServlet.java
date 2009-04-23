package org.astrogrid.security.community;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.security.rfc3820.ProxyCertificateFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

/**
 * A servlet to handle requests for account details. It implements v3 of the
 * AstroGrid-community accounts protocol.
 * <p>
 * Certificate chains may be fetched with a posted request to a URI ending in
 * <i>/proxy</i>. 
 * The request parameters are
 * </p><dl>
 * <dt>username</dt>
 * <dd>The user-name of the account as registered in the community database.</dd>
 * <dt>password</dt>
 * <dd>The user-name of the account as registered in the community database.</dd>
 * <dt>key</dt>
 * <dd>The public key to be incorporated in the proxy certificate.</dd>
 * <dt>lifetime</dt>
 * <dd>The duration of validity of the proxy certificate, in whole seconds.</dd>
 * </dl><p>
 * The key must be sent in PEM format.
 * </p><p>
 * On successful completion, the body of the response is the user's certificate
 * chain, in PEM encoding, sent as MIME type text/plain. The first certificate
 * in the chain is a newly-created proxy-certificate containing the public key
 * given in the request. The rest of the chain contains zero or more proxy
 * certificates and one or more end-entity certificate (EEC). The last
 * certificate in the chain is signed by one of the community's trust anchors;
 * the requestor is assumed to have copies of those anchors.
 * <p>
 * The URI for the home-space may be got from a URI ending in <i>/home</i>.
 * This request needs no parameters.
 * <p>
 * On successful completion, there is no content in the body of the response
 * but the client is redirected to the home-space location. The URI for the
 * redirection is typically in the ivo or vos schemes, so HTTP clients will
 * not be able to follow the redirection automatically. They will need to
 * read the <i>Location</i> header and delegate to an appropriate client
 * libary.
 *
 * @author Guy Rixon
 */
public class AccountServlet extends HttpServlet {
  static private Log log = LogFactory.getLog(AccountServlet.class);

  /**
   * The accounts in the dummy community. Keys are user-names, values
   * contain everything known about a user.
   */
  private Map<String,Account> pond;

  /**
   * The factory for EECs.
   */
  CertificateFactory certificateFactory;

  /**
   * The factory for proxy certificates.
   */
  ProxyCertificateFactory proxyFactory;
  
  /**
   * Creates the objects reused in each request.
   *
   * @throws RuntimeException If the reusuable objects cannot be set up.
   */
  @Override
  public void init() {
    
    // Have to use this JCE provider otherwise PEMReader fails.
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }

    try {
      certificateFactory = CertificateFactory.getInstance("X509");
    } catch (CertificateException ex) {
      throw new RuntimeException("Configuration error", ex);
    }

    proxyFactory = new ProxyCertificateFactory();

    // Set up account details.
    pond = new HashMap<String,Account>();
    pond.put("frog", new Account("frog", "croakcroak", certificateFactory));
  }
  
  /** 
   * Handles the HTTP <code>POST</code> method.
   * 
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doPost(HttpServletRequest  request, 
                        HttpServletResponse response) throws IOException {

    try {
      String userName = null;
      String path = request.getPathInfo();

      // Can't post to the root of the accounts tree.
      if (path == null) {
        response.sendError(response.SC_METHOD_NOT_ALLOWED);
      }

      // Post to the user resource: it's a password change.
      if (path.indexOf('/') == path.lastIndexOf('/')) {
        userName = path.substring(1);
        postToUser(request, userName, response);
      }
      else if (path.endsWith("/proxy")) {
        userName = path.substring(1, path.lastIndexOf("/proxy"));
        postToProxy(request, userName, response);
      }
      else if (path.endsWith("/home")) {
        response.sendError(response.SC_METHOD_NOT_ALLOWED);
      }
      else {
        log.info("Request to account servlet for " + path + " is no good.");
        response.sendError(response.SC_NOT_FOUND);
        return;
      }
    }
    catch (AccessControlException e) {
      e.printStackTrace();
      response.sendError(response.SC_FORBIDDEN, e.getMessage());
      return;
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
      response.sendError(response.SC_BAD_REQUEST, e.getMessage());
      return;
    }
    catch (Exception e) {
      e.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
      return;
    }
  }

  /**
   * Checks the password for a given user-name. Returns silently if the
   * check is passed; otherewise, throws exceptions.
   *
   * @param userName The user identity to be authenticated.
   * @param password The password to check.
   * @return The account details.
   * @throws FileNotFoundException If the user is unknown.
   * @throws AccessControlException If the password is not correct.
   */
  private Account authenticate(String userName,
                               String password) throws FileNotFoundException,
                                                       AccessControlException {
    Account account = pond.get(userName);
    if (account == null) {
      throw new FileNotFoundException(userName + " is not a community member");
    }
    if (password.equals(account.getPassword())) {
      return account;
    }
    else {
      throw new AccessControlException("Password is invalid");
    }
  }
  
  /**
   * Handles a posted request to a proxy resource.
   */
  private void postToProxy(HttpServletRequest  request,
                           String              userName,
                           HttpServletResponse response) throws Exception {
    
    String password = request.getParameter("password");
    if (password == null|| password.length() == 0) {
      log.info("Certificate request failed for " + userName +
               "; no password was given.");
      throw new AccessControlException("No password was given.");
    }
    
    String pemKey = request.getParameter("key");
    if (pemKey == null || pemKey.length() == 0) {
      log.info("Certificate request failed for " + userName +
               "; no public key was given.");
      throw new IllegalArgumentException("No public key was given.");
    }
    
    int lifetime;
    String requestedLifetime = request.getParameter("lifetime");
    if (requestedLifetime == null || requestedLifetime.length() == 0) {
      log.info("Certificate request failed for " + userName +
               "; no lifetime was given.");
      throw new IllegalArgumentException("No lifetime was given.");
    }
    else {
      lifetime = Integer.parseInt(requestedLifetime);
    }
    
    // Extract the public key from its parameter.
    // The key is given in PEM format (base-64 encoded DER).
    PublicKey key = null;
    try {
      PEMReader pr = new PEMReader(new StringReader(pemKey));
      System.out.println("got key");
      System.out.println("reading key");
      Object o = pr.readObject();
      if (o instanceof PublicKey) {
        key = (PublicKey) o;
      }
      else {
        log.info("Certificate request failed for " + userName +
                 "; the public key is not a valid PEM object.");
        throw new IllegalArgumentException("The public key is not a valid PEM object.");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("The public key is not a valid PEM object.");
    }
    
    // Check the password.
    authenticate(userName, password);
    log.debug(userName + " has provided a valid password.");
    
    // Generate a proxy certificate using the given key.
    // Make up a certificate chain containing the proxy.
    CertPath chain = null;
    int nCerts = 0;
    List certificates = 
        getCertificateChain(userName, password, key, lifetime);
    chain = CertificateFactory.getInstance("X509").generateCertPath(certificates);
    nCerts =  certificates.size();
    log.debug("Proxy certificate has been generated; now " + 
              nCerts + " certificates to send");
    
    // Send the certificate chain in PkiPath format.
    response.setContentType("application/octet");
    response.setStatus(response.SC_OK);
    try {
      response.getOutputStream().write(chain.getEncoded("PkiPath"));
      response.flushBuffer();
      log.info(nCerts + 
               " certificates have been sent to the client for " +
               userName);
    } catch (CertificateEncodingException ex) {
      ex.printStackTrace();
      throw new Exception("Failed to encode the certificate chain as PkiPath.");
    }
    catch (IOException ex) {
      ex.printStackTrace();
      throw new Exception("Failed to send certificate chain to the client.");
    }
  }

  /**
   * Produces the user's certificate chain extended with a newly-created
   * proxy certificate.
   *
   * @param userName The user name.
   * @param password The password, in plain text.
   * @param proxyKey The key to be written into the new proxy-certficate.
   * @param lifetime The duration of validaity, in seconds, for the new proxy.
   * @return A certificate chain starting with the new proxy.
   */
  public List getCertificateChain(String    userName,
                                  String    password,
                                  PublicKey proxyKey,
                                  int       lifetime) throws GeneralSecurityException,
                                                             FileNotFoundException {

    // Find the account and make sure that the caller is allowed to use it.
    Account account = authenticate(userName, password);
    List<X509Certificate> certificates = account.getCertificateChain();
    if (certificates.size() == 0) {
      throw new GeneralSecurityException(userName +
                                         " has an empty certificate-chain.");
    }

    // Generate a proxy certificate signed by the certificate on the
    // front of the stored chain. Add this to the front of the chain.
    log.debug("Got " + certificates.size() + " certificates.");
    PrivateKey privateKey = account.getPrivateKey();
    proxyFactory.extendCertificateChain(certificates,
                                        privateKey,
                                        proxyKey,
                                        lifetime,
                                        false); // GSI-3 proxy, not RFC-3820.
    return certificates;
  }
  
  protected void postToUser(HttpServletRequest  request,
                            String              userName,
                            HttpServletResponse response) throws AccessControlException,
                                                                 FileNotFoundException {
      
    String oldPassword = getParameter(request, "oldPassword");
    String newPassword = getParameter(request, "newPassword");
    String rptPassword = getParameter(request, "repeatNewPassword");
    
    assert userName != null;
    
    if (oldPassword == null) {
      throw new IllegalArgumentException("No value was given for the current password.");
    }
    else if (newPassword.length() < 7) {
       throw new IllegalArgumentException("Your password must be at least 7 characters long.");
    }
    else if (newPassword == null) {
       throw new IllegalArgumentException("No value was given for the new password.");
    }
    else {
      System.out.println("Changing password for " + userName);
      Account a = authenticate(userName, oldPassword);
      a.setPassword(newPassword);
    }
  }
  
  
  /** 
   * Handles the HTTP GET method.
   * 
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doGet(HttpServletRequest  request, 
                       HttpServletResponse response) throws ServletException, 
                                                            IOException {
    
    // Parse the requested URL to check that this request is for a homespace
    // and to find the user's name.
    String userName = null;
    String path = request.getPathInfo();
    if (path.startsWith("/") && path.endsWith("/home")) {
      userName = path.substring(1, path.lastIndexOf("/home"));
    }
    else {
      log.info("Request to account servlet for " + path + " is no good.");
      response.sendError(response.SC_NOT_FOUND);
      return;
    }

    // Return something totally bogus, but in the correct syntax.
    String homeSpace = "vos://nix.nada.not-there!bogus/missing/awol";
    response.setHeader("Location", homeSpace);
    response.setStatus(response.SC_SEE_OTHER);
    
  }
  
 
  /**
   * Gets a parameter value.
   * Missing parameters and parameters with empty values come back as null strings.
   * Leading and trailing white space is removed from the values.
   */
  private String getParameter(HttpServletRequest request, String parameter) {
    String value = request.getParameter(parameter);
    if (value == null) {
      return null;
    }
    else {
      String trimmed = value.trim();
      if (trimmed.length() == 0) {
        return null;
      }
      else {
        return trimmed;
      }
    }
  }

  /**
   * Produces a PEMReader.
   * The password for reading private keys is initialized.
   *
   * @param reader The reader for the input stream to be read.
   * @param password The unencrypted, undigested password for the private key.
   * @return The reader.
   */
  private PEMReader getPemReader(Reader reader, String password) {
    PasswordFinder f = new Password(password);
    return new PEMReader(reader, f);
  }

  public class Password implements PasswordFinder {

    private String password;

    public Password(String password) {
      this.password = password;
    }

    public char[] getPassword() {
      return this.password.toCharArray();
    }
  }
  
}
