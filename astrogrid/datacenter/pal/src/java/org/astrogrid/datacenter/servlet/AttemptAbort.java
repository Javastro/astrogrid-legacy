/*
 * $Id: AttemptAbort.java,v 1.5 2004/11/10 22:01:50 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;
import org.astrogrid.webapp.DefaultServlet;

/**
 * A servlet that attempts to abort the given query with the given ID
 *
 * @author mch
 */
public class AttemptAbort extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

      String queryId = request.getParameter("ID");

      if (queryId == null) {
         doError(response, "No ID given", null);
         return;
      }
      
      try {
         server.abortQuery(ServletHelper.getUser(request), queryId);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th);
         doError(response, "Attempting Abort of Query ID="+queryId,th);
      }
   }


}
