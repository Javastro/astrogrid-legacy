package org.astrogrid.community.webapp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl;
import org.astrogrid.community.server.sso.CredentialStore;
import org.astrogrid.config.SimpleConfig;

/**
 * A servlet to update or delete user-accounts.
 *
 * @author Guy Rixon
 */
public class AccountAdminServlet extends HttpServlet {
  
  /** Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
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
    AccountManagerImpl ami = new AccountManagerImpl();
    AccountData ad = getBasicAccount(userName);
    try {
      ami.delAccount(ad.getIdent());
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
    AccountManagerImpl ami = new AccountManagerImpl();
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
      ad.setHomeSpace(null);
    }
    else if (hasContent(homeSpace)) {
      ad.setHomeSpace(homeSpace);
    }
    try {
      if ("new".equals(homeSpace)) {
        ami.allocateSpace(ad);
      }
      ami.setAccount(ad);
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
        CredentialStore cs = new CredentialStore();
        
        String oldPassword = cs.getPassword(userName);
        cs.resetDbPassword(userName, password);
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
    String community = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.ident");
    int slash = community.indexOf('/');
    if (slash != -1) {
      community = community.substring(0, slash);
    }
    String accountIvorn = "ivo://" + community + "/" + userName;
    
    AccountManagerImpl ami = new AccountManagerImpl();
    try {
      return ami.getAccount(accountIvorn);
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
