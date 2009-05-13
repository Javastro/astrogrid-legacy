/*
 * $Id: AskCount.java,v 1.1 2009/05/13 13:20:33 gtr Exp $
 */

package org.astrogrid.dataservice.service.servlet;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.webapp.DefaultServlet;

/**
 * A servlet for asking a query ynchronously, returning just the count.
 * The parameters are:
 * AdqlUrl, AdqlSql or AdqlXml that specify the query to be run.
 *
 * @author mch
 */
public class AskCount extends DefaultServlet {
   
   DataServer server = new DataServer();
   
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      Query query = null;
      
      try {
         query = SubmitQuery.makeQuery(request);
         Principal user = ServletHelper.getUser(request);
         
         long count = server.askCount(user, query, request.getRemoteAddr()+" via AskCount servlet");
         response.getWriter().write(""+count);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+" submitting query",th);
         doError(response, "Submitting "+query+" -> "+ServletHelper.makeReturnSpec(request),th);
      }
   }
   
}
