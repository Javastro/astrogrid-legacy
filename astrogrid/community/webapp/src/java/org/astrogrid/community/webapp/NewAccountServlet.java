package org.astrogrid.community.webapp;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl;
import org.astrogrid.community.server.sso.CredentialStore;

/**
 * A servlet to add a basic account to the user database.
 *
 * @author Guy Rixon
 */
public class NewAccountServlet extends HttpServlet {
  
  /** Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doPost(HttpServletRequest request, 
                        HttpServletResponse response) throws IOException {
    String userName = request.getParameter("userName");
    if (userName == null || userName.trim().length() == 0) {
      response.sendError(response.SC_BAD_REQUEST, "No user-name was given.");
      return;
    }
    
    String commonName  = request.getParameter("commonName");
    String description = request.getParameter("description");
    String email       = request.getParameter("email");
    String homeSpace   = request.getParameter("homeSpace");
    String password    = request.getParameter("password");
    
    String authority = new CommunityConfiguration().getPublishingAuthority();
    String accountIvorn = "ivo://" + authority + "/" + userName;
    AccountData ad = new AccountData(accountIvorn);
    ad.setDescription(description);
    ad.setDisplayName(commonName);
    ad.setEmailAddress(email);
    ad.setHomeSpace(homeSpace);
    
    try {
      AccountManagerImpl ami = new AccountManagerImpl();
      ami.addAccount(ad);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                         ex.getMessage());
      return;
    }
    
    try {
      CredentialStore cs = new CredentialStore();
      cs.resetDbPassword(userName.trim(), password);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR,
                         ex.getMessage());
      return;
    }
    
    response.sendRedirect("account-update.jsp?userName=" + userName);
  }
}
