package org.astrogrid.community.webapp;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.server.ca.CertificateAuthority;
import org.astrogrid.community.server.ca.UserFiles;
import org.astrogrid.community.server.sso.CredentialStore;
import org.astrogrid.config.SimpleConfig;

/**
 * A servlet to operate the certificate authority (CA).
 * <p>
 * The servlet responds to requests to two URIs. A request to the main
 * CA URI (which is defined in web.xml) is a request to issue credentials for
 * a user. A request to the child "/passphrase" of the CA URI is a request
 * to load the CA credentials from disc, unlocking the private key with the
 * CA passphrase. The CA must be loaded before it can issue credentials; once
 * loaded, it is stored in the HTTP session.
 * <p>
 * A request posted to the passphrase URI must carry the CA passphrase, 
 * unencrypted and undigested, in the parameter "passphrase".
 * There is no body content in a successful response, and the user agent is
 * offered a redirection to the accounts page.
 * <p>
 * A request posted to the CA URI must carry parameters as follows.
 * </p><dl>
 * <dt>username</dt>
 * <dd>The user name of the user to receive the  credentials. This user name 
 * must not be qualified with the community name and should not be an IVORN, 
 * e.g. "fred" rather than "fred@foo.bar/community" or 
 * "ivo://fed@foo.bar/community".</dd>
 * <dt>commonname</dt>
 * <dd>The common name (a.k.a "display name) for the user.</dd>
 * <dt>password</dt>
 * <dd>The password, unencrypted and undigested, for the user's account.</dd>
 * </dl><p>
 * There is no body content in a successful response, and the user agent is
 * offered a redirection to the accounts page.
 * <p>
 * Both the resources have the same representation when got: a simple string
 * stating the status of the CA. This "not enabled" if there is no CA object
 * in the session; the root DN of the CA is there is such an object; or
 * "corrupt!" in the unlikely case that an improper object has been inserted
 * into the session where the CA is expected. These forms are specifically
 * chosen to appear in a status banner at the head of pages, so they are very
 * short. They must always be a single line of text.
 *
 * @author Guy Rixon
 */
public class CaServlet extends HttpServlet {
  
  /**
   * The name of the session attribute holding the CA object.
   */
  protected static final String CA_ATTRIBUTE = "org.astrogrid.community.ca";
  
  /** Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    if (request.getServletPath().endsWith("/passphrase")) {
      enableCa(request, response);
    }
    else {
      useCa(request, response);
    }
  }
  
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    response.setContentType("text/plain");

    try {
      CertificateAuthority ca = 
          (CertificateAuthority) request.getSession().getAttribute(CA_ATTRIBUTE);
      if (ca == null) {
        response.getWriter().write("not enabled");
      }
      else {
        response.getWriter().write(ca.getRootDn());
      }
    } 
    catch (ClassCastException e) {
      response.getWriter().write("corrupt!");
    }
  }

  private void enableCa(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    String passphrase = request.getParameter("passphrase");
    if (passphrase == null || passphrase.length() == 0) {
      response.sendError(response.SC_BAD_REQUEST, "No passphrase was given.");
      return;
    }
    
    String keyFile = null;
    try {
      keyFile = 
          SimpleConfig.getSingleton().getString("org.astrogrid.community.cakey");
    }
    catch (Exception e) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, 
                         "The CA key-file is not configured");
      return;
    }
    
    String certificateFile = null;
    try {
      certificateFile = 
          SimpleConfig.getSingleton().getString("org.astrogrid.community.cacert");
    }
    catch (Exception e) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, 
                         "The CA certificate-file is not configured");
      return;
    }
 
    String serialFile = null;
    try {
      
      serialFile = 
          SimpleConfig.getSingleton().getString("org.astrogrid.community.caserial");
    }
    catch (Exception e) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, 
                         "The CA serial-number-file is not configured");
      return;
    }
    
    // Create a CA. Set it as a session attribute.
    try {
      CertificateAuthority ca = 
          new CertificateAuthority(passphrase,
                                   new File(keyFile),
                                   new File(certificateFile),
                                   new File(serialFile),
                                   new File("."));
      request.getSession().setAttribute(CA_ATTRIBUTE, ca);
    }
    catch (Exception e) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                         "The CA isn't right: " + e.getMessage());
    }
    
    response.setHeader("Location", 
                         request.getContextPath() + "/admin/account-list.jsp");
    response.setStatus(response.SC_SEE_OTHER);
  }

  /**
   * Generates credentials for a user named in the request parameters.
   * The credentials go to a configured directory-tree.
   * This method looks up the user in the community DB to get the user's
   * common name; therefore, it fails to make credentials if the given
   * user-name is unknown to the community. Further, the user's password
   * is read from the user database and used to encrypt the stored
   * private key; the request will be denied if the requestor
   * does not give the right password.
   */
  private void useCa(HttpServletRequest request, 
                     HttpServletResponse response) throws IOException {
    
    // Find the directory for the credentials from the configuration.
    // Its configuration key refers to MyProxy, even though the community
    // may not be providing a Myproxy service; the community REST service
    // for SSO uses the same directory lay-out.
    String credentialDirectoryName =
        SimpleConfig.getSingleton().getString("org.astrogrid.community.myproxy", null);
    if (credentialDirectoryName == null) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                         "The directory for writing user credentials is not configured.");
    }
    File credentialDirectory = new File(credentialDirectoryName);
    if (!credentialDirectory.exists()) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                         "The configured directory for writing user credentials does not exist.");
    }
    
    // Recover the CA from the session.
    CertificateAuthority ca = null;
    try {
      ca = (CertificateAuthority) request.getSession().getAttribute(CA_ATTRIBUTE);
      if (ca == null) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                           "The CA is not enabled in your HTTP session.");
        return;
      }
    } 
    catch (ClassCastException e) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                         "An improper object is set for the CA.");
      return;
    }
    
    // Get the user name from the HTTP request.
    String userName = request.getParameter("userName");
    if (userName == null || userName.length() == 0) {
      response.sendError(response.SC_BAD_REQUEST, "No user-name was given.");
      return;
    }
    
    // Get the common name from the HTTP request.
    String commonName = request.getParameter("commonName");
    if (commonName == null || commonName.length() == 0) {
      response.sendError(response.SC_BAD_REQUEST, "No common name was given.");
      return;
    }
    
    // Get the password from the community database.
    String password = null;
    try {
      password = new CredentialStore().getPassword(userName);
    }
    catch (Exception e) {
      e.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, 
                         "The password for " + 
                         userName + 
                         " could not be read from the community database: " + e);
      return;
    }
    
    // Create and store the credential. Given that the user-name, account
    // and password have been validated above, any problem at this stage is
    // a fault in the community code or in its configuration.
    try {
      ca.generateUserCredentials(userName, 
                                 commonName,
                                 password,
                                 new UserFiles(credentialDirectory, userName));
    } catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
    
    response.setHeader("Location", 
                       request.getContextPath() + 
                           "/admin/account-update.jsp?userName=" + 
                           userName);
    response.setStatus(response.SC_SEE_OTHER);
  }
   
}
