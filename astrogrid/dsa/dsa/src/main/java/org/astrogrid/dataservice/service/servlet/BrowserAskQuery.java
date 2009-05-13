/*
 * $Id: BrowserAskQuery.java,v 1.1.1.1 2009/05/13 13:20:33 gtr Exp $
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
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.query.Query;
import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.astrogrid.slinger.targets.WriterTarget;
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
            response.setContentType(MimeTypes.PLAINTEXT);
            response.getWriter().write(""+count);
         }
         else {
            if (query.getTarget() == null) {
               query.getResultsDef().setTarget(new WriterTarget(response.getWriter(), false));
               // Need to do this here;  the table writer doesn't do it
               // for us.
               response.setContentType(query.getResultsDef().getFormat());
                     
               server.askQuery(user, query, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via BrowserAskQuery servlet");
            }
            else {
               // Cache credentials for use in call to VOSpace.
               HttpsServiceSecurityGuard sg = new HttpsServiceSecurityGuard();
               sg.loadHttpsAuthentication(request);
               sg.loadDelegation();
               query.setGuard(sg);
               
               //target given, so ask query, returning status when complete
               String id = server.submitQuery(user, query, request.getRemoteAddr()+" via BrowserAskQuery servlet");
               response.sendRedirect("admin/queryStatus.jsp?ID="+id);
            }
         }
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+" submitting query",th);
         doError(response, "Submitting "+query+" -> "+ServletHelper.makeReturnSpec(request),th);
      }
   }
   
}
