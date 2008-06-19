package org.astrogrid.community.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl;
import org.astrogrid.config.SimpleConfig;

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
  protected void doGet(HttpServletRequest request, 
                       HttpServletResponse response) throws ServletException, 
                                                            IOException {
    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();
    
    String[] userNames = new AccountManagerImpl().getUserNames();
    for(int i = 0;i < userNames.length; i++) {
      AccountData account = getBasicAccount(userNames[i]);
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
    }
  }
  
  /**
   * Raises the basic account data from the community database.
   * If this fails, most of the fields of the result will be null.
   *
   * @param userName The userName, unqualified (fred rather than ivo://fred@foo).
   * @return The account object (never null; empty on DB error).
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
}
