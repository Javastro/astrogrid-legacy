/*
 * $Id: AttemptAbort.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 */

package org.astrogrid.dataservice.service.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
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
         
         //forward to the status pages
         request.getRequestDispatcher("http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/queryStatus.jsp?"+queryId).forward(request, response);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+" aborting query "+queryId,th);
         doError(response, "Attempting Abort of Query ID="+queryId,th);
      }
   }


}
