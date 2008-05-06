package org.astrogrid.community.webapp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.AccessControlException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl;
import org.astrogrid.community.server.sso.ProxyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;

/**
 * A servlet to handle requests for account details. It implements v2 of the
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
  
  private ProxyFactory factory;
  
  /**
   * Creates the objects reused in each request.
   *
   * @throws RuntimeException If the reusuable objects cannot be set up.
   */
  public void init() {
    
    // Have to use this JCE provider otherwise PEMReader fails.
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
    
    try {
      this.factory = new ProxyFactory();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't set up the factory for proxy certificates", e);
    }
  }
  
  /** 
   * Handles the HTTP <code>POST</code> method.
   * 
   * @param request servlet request
   * @param response servlet response
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    
    String userName = null;
    String path = request.getPathInfo();
    if (path.startsWith("/") && path.endsWith("/proxy")) {
      userName = path.substring(1, path.lastIndexOf("/proxy"));
    }
    else {
      log.info("Request to account servlet for " + path + " is no good.");
      response.sendError(response.SC_NOT_FOUND);
      return;
    }
    
    String password = request.getParameter("password");
    if (password == null|| password.length() == 0) {
      log.info("Certificate request failed for " + userName +
               "; no password was given.");
      response.sendError(response.SC_BAD_REQUEST, "No password was given.");
      return;
    }
    
    String pemKey = request.getParameter("key");
    if (pemKey == null || pemKey.length() == 0) {
      log.info("Certificate request failed for " + userName +
               "; no public key was given.");
      response.sendError(response.SC_BAD_REQUEST, "No public key was given.");
      return;
    }
    
    int lifetime;
    String requestedLifetime = request.getParameter("lifetime");
    if (requestedLifetime == null || requestedLifetime.length() == 0) {
      log.info("Certificate request failed for " + userName +
               "; no lifetime was given.");
      response.sendError(response.SC_BAD_REQUEST, "No lifetime was given.");
      return;
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
        response.sendError(response.SC_BAD_REQUEST, "The public key is not a valid PEM object.");
        return;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      response.sendError(response.SC_BAD_REQUEST, "The public key is not a valid PEM object.");
      return;
    }
    
    // Check the password.
    try {
      this.factory.authenticate(userName, password);
      log.debug(userName + " has provided a valid password.");
    }
    catch (AccessControlException e) {
      e.printStackTrace();
      response.sendError(response.SC_FORBIDDEN, e.getMessage());
      return;
    }
    
    // Generate a proxy certificate using the given key.
    // Make up a certificate chain containing the proxy.
    List certificates = new ArrayList(2);
    CertPath chain = null;
    int nCerts = 0;
    try {
      chain = this.factory.createProxy(userName, password, key, lifetime);
      nCerts =  chain.getCertificates().size();
      log.debug("Proxy certificate has been generated; now " + 
                nCerts + " certificates to send");
    }
    catch (Exception e) {
      e.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
      return;
    }
    
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
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, 
                         "Failed to encode the certificate chain as PkiPath.");
    }
    catch (IOException ex) {
      ex.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, 
                         "Failed to send certificate chain to the client.");
    }
  }
  
  /** 
   * Handles the HTTP GET method.
   * 
   * @param request servlet request
   * @param response servlet response
   */
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
    
    // Get the user's account-record from the community DB.
    // Note that the API for the DB accepts new-style
    // account-IVORNs.
    StringBuilder accountName;
    try {
      accountName = new StringBuilder("ivo://");
      accountName.append(userName);
      accountName.append('@');
      accountName.append(CommunityIvornParser.getLocalIdent());
    } catch (Exception ex) {
      ex.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR);
      return;
    }   
    AccountManagerImpl ami = new AccountManagerImpl();
    AccountData account = null;
    try {
      account = ami.getAccount(accountName.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR);
      return;
    }
    
    // If a homespace is recorded for this user, redirect the
    // client to that URI. Otherwise, treat the current resource
    // as not found.
    if (account.getHomeSpace() == null) {
      response.sendError(response.SC_NOT_FOUND);
    }
    else {
      response.setHeader("Location", account.getHomeSpace());
      response.setStatus(response.SC_SEE_OTHER);
    }
    
  }
}
