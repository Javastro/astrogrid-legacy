/*
 * $Id: BrowserAskQuery.java,v 1.1 2005/02/17 18:37:35 mch Exp $
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
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.webapp.DefaultServlet;

/**
 * A servlet for asking a query synchronously.  Just like AskQuery except that
 * it forwards the browser to the status page if a target is given.
 * The parameters are:
 * AdqlUrl, AdqlSql or AdqlXml that specify the query to be run.
 * Target: where the results are to be sent (empty=return to response)
 * Format: what format the results should be sent in (eg VOTABLE, CSV - defaults to VOTABLE)
 *
 * @author mch
 */
public class BrowserAskQuery extends DefaultServlet {
   
   DataServer server = new DataServer();
   
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      Query query = null;
      
      try {
         query = SubmitQuery.makeQuery(request);
         Principal user = ServletHelper.getUser(request);
         
         if (ServletHelper.isCountReq(request)) {
            long count = server.askCount(user, query, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via BrowserAskQuery servlet");
            response.getWriter().write(""+count);
         }
         else {
            if (query.getTarget() == null) {
               //no target, return results to browser with the first mime type requested (will assume it can be done...)
               response.setContentType(query.getResultsDef().getFormat());
               query.getResultsDef().setTarget(TargetMaker.makeTarget(response.getWriter(), false));
               server.askQuery(user, query, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via BrowserAskQuery servlet");
            }
            else {
               //target given, so ask query, returning status when complete
               String id = server.submitQuery(user, query, request.getRemoteAddr()+" via BrowserAskQuery servlet");
               response.sendRedirect("queryStatus.jsp?ID="+id);
            }
         }
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+" submitting query",th);
         doError(response, "Submitting "+query+" -> "+ServletHelper.makeReturnSpec(request),th);
      }
   }
   
}
