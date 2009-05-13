/*
 * $Id: AskQuery.java,v 1.1.1.1 2009/05/13 13:20:33 gtr Exp $
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
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.webapp.DefaultServlet;

/**
 * A servlet for asking a query ynchronously.
 * The parameters are:
 * AdqlUrl, AdqlSql or AdqlXml that specify the query to be run.
 * Target: where the results are to be sent (empty=return to response)
 * Format: what format the results should be sent in (eg VOTABLE, CSV - defaults to VOTABLE)
 *
 * @author mch
 */
public class AskQuery extends DefaultServlet {
   
   DataServer server = new DataServer();
   
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      Query query = null;
      
      try {
         query = SubmitQuery.makeQuery(request);
         Principal user = ServletHelper.getUser(request);
         
         if (query.getTarget() == null) {
            //no target, return results to browser with the first mime type requested (will assume it can be done...)
            response.setContentType(query.getResultsDef().getFormat());
            query.getResultsDef().setTarget(new WriterTarget(response.getWriter(), false));
            server.askQuery(user, query, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via AskQuery servlet");
         }
         else {
            //target given, so return just the id
            response.setContentType("text/html");
            server.askQuery(user, query, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via AskQuery servlet");
            response.getWriter().println(
               "<html>"+
               "<head><title>Query Asked</title></head>"+
               "<body>Query Asked</body>");
         }
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+" submitting query",th);
         doError(response, "Submitting "+query+" -> "+ServletHelper.makeReturnSpec(request),th);
      }
   }
   
}
