/*
 * $Id: AttemptAbort.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;

/**
 * A servlet that attempts to abort the given query with the given ID
 *
 * @author mch
 */
public class AttemptAbort extends StdServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

      String queryId = request.getParameter("ID");

      if (queryId == null) {
         doError(response, "No ID given", null);
         return;
      }
      
      try {
         server.abortQuery(Account.ANONYMOUS, queryId);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th);
         doError(response, "Attempting Abort of Query ID="+queryId,th);
      }
   }


}
