/*
 * $Id: AskQuery.java,v 1.1 2004/10/12 23:09:53 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;
import org.astrogrid.slinger.TargetIndicator;

/**
 * A servlet for asking a query ynchronously.
 * The parameters are:
 * AdqlUrl, AdqlSql or AdqlXml that specify the query to be run.
 * Target: where the results are to be sent
 * Format: what format the results should be sent in (eg VOTABLE, CSV - defaults to VOTABLE)
 *
 * @author mch
 */
public class AskQuery extends StdServlet {
   
   DataServer server = new DataServer();
   
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      Query query = null;
      
      try {
         query = SubmitQuery.makeQuery(request);
         Account user = ServletHelper.getUser(request);
         
         if (query.getTarget() == null) {
            //no target, return results to browser
            if (query.getResultsDef().getFormat().equals("VOTABLE")) {
               response.setContentType("text/xml");
            }
            else if (query.getResultsDef().getFormat().equals("HTML")) {
               response.setContentType("text/html");
            }
            else {
               response.setContentType("text/plain");
            }
            query.getResultsDef().setTarget(TargetIndicator.makeIndicator(response.getWriter()));
            server.askQuery(user, query);
         }
         else {
            //target given, so ask query, returning status when complete
            response.setContentType("text/html");
            server.askQuery(user, query);
            response.getWriter().println(
               "<html>"+
               "<head><title>Query Asked</title></head>"+
               "<body>Query Asked</body>");
         }
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th);
         doError(response, "Submitting "+query+" -> "+ServletHelper.makeReturnSpec(request),th);
      }
   }
   
}
