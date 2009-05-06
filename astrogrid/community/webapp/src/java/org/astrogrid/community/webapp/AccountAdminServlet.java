package org.astrogrid.community.webapp;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl;
import org.astrogrid.community.server.sso.CredentialStore;
/**
 * A servlet to update or delete user-accounts.
 *
 * @author Guy Rixon
 */
public class AccountAdminServlet extends HttpServlet {

  private CredentialStore credentialStore;

  private AccountManagerImpl accountManager;

  @Override
  public void init() {
    try {
      credentialStore = new CredentialStore();
      accountManager = AccountManagerImpl.getDefault();
    } catch (GeneralSecurityException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /** Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    String userName = request.getParameter("userName");
    if (userName == null || userName.trim().length() == 0) {
      response.sendError(response.SC_BAD_REQUEST, "No user-name was given.");
      return;
    }
    
    if (hasContent(request.getParameter("delete"))) {
      delete(request, userName, response);
    }
    else {
      update(request, userName, response);
    }
  }
  
  protected void delete(HttpServletRequest  request,
                        String              userName,
                        HttpServletResponse response) throws ServletException, 
                                                             IOException {
    AccountData ad = getBasicAccount(userName);
    try {
      accountManager.delAccount(ad.getIdent());
    }
    catch (Exception e) {
      e.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                         e.getMessage());
      return;
    }
    request.getRequestDispatcher("/admin/account-list.jsp").forward(request, response);
  }
    
  protected void update(HttpServletRequest  request,
                        String              userName,
                        HttpServletResponse response) throws IOException, 
                                                             ServletException {
    String commonName  = request.getParameter("commonName");
    String description = request.getParameter("description");
    String email       = request.getParameter("email");
    String homeSpace   = request.getParameter("homeSpace");
    String password    = request.getParameter("password");
    
    // Update the account table of the community database.
    // Note the handling of the special keyword for allocating
    // a new homeSpace. This distinguishes between recording
    // a homeSpace in the community database and getting the
    // VOSpace itself to allocate a new one.
    try {
      AccountData ad = getBasicAccount(userName);
      if (hasContent(commonName)) {
        ad.setDisplayName(commonName);
      }
      if (hasContent(description)) {
        ad.setDescription(description);
      }
      if (hasContent(email)) {
        ad.setEmailAddress(email);
      }
      if ("new".equals(homeSpace)) {
        password = credentialStore.getPassword(userName);
        homeSpace = 
            accountManager.allocateHomespace(ad.getIdent(), userName, password);
        ad.setHomeSpace(homeSpace);
      }
      else if (hasContent(homeSpace)) {
        ad.setHomeSpace(homeSpace);
      }
      accountManager.setAccount(ad);
      System.out.println("The details for " + userName + " are updated.");
    }
    catch (Exception e) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                         e.getMessage());
      return;
    }
    
    // Update the passwords table of the community database.
    if (hasContent(password)) {
      try {
        credentialStore.resetDbPassword(userName, password);
        System.out.println("The password for " + userName + " is updated.");
      }
      catch (Exception e) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                           e.getMessage());
        return;
      }
    }
    
    // Credentials aren't updated here. See the CaServlet.
    
    // Pass control to the JSP that renders one account.
    request.getRequestDispatcher("/admin/account-update.jsp").forward(request, response);
  }
  
  /**
   * Raises the basic account data from the community database.
   * If this fails, most of the fields of the result will be null.
   */
  protected AccountData getBasicAccount(String userName) {
    String authority = new CommunityConfiguration().getPublishingAuthority();
    String accountIvorn = "ivo://" + authority + "/" + userName;
    
    try {
      return accountManager.getAccount(accountIvorn);
    }
    catch (Exception e) {
      return new AccountData(accountIvorn);
    }
  }
  
  /**
   * Determines whether a string received as a parameter has any real content.
   * Nulls, zero-length strings and strings containing only white space are
   * deemed not to have content.
   */
  private boolean hasContent(String s) {
    return (s != null && s.trim().length() > 0);
  }
  
}
