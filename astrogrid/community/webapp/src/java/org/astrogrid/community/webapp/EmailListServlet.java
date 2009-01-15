package org.astrogrid.community.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl;

/**
 * A servlet to generate an email-address list of all members of the community.
 * The list is emitted as a document of type text/plain. Each line contains
 * one email address and all lines are terminated by new-line characters.
 * Where a usre has registered both an email address and a common name (this
 * is normal), then the line is of the format "name <email-address>". Where
 * a user has registered only the address, then line contains just the address.
 * If a user has not registered an email address then that user is silently
 * excluded from the list.
 *
 * @author Guy Rixon
 * @version
 */
public class EmailListServlet extends HttpServlet {
  
  /** 
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doGet(HttpServletRequest request, 
                       HttpServletResponse response) throws ServletException, 
                                                            IOException {
    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();

    // Iterate over user-names, print the email addresses where available.
    // Names for which the account records are unavailable are logged and
    // skipped; the client does not learn of these.
    AccountManagerImpl ami = new AccountManagerImpl();
    String[] userNames = ami.getUserNames();
    for(int i = 0;i < userNames.length; i++) {
      try {
        AccountData account = ami.getAccountByUserName(userNames[i]);
        String email = account.getEmailAddress();
        String name = account.getDisplayName();
        if (email != null && email.trim().length() > 0) {
          if (name != null && name.trim().length() > 0) {
            out.println(name + " <" + email + ">");
          }
          else {
            out.println(email);
          }
        }
      } catch (Exception ex) {
        LogFactory.getLog(EmailListServlet.class.getName()).warn(ex);
      }
      
    }
  }
  
}
