/*
 * $Id: SubmitCone.java,v 1.5 2004/10/25 10:43:12 jdt Exp $
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
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.slinger.TargetIndicator;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;

/**
 * A servlet for processing Cone Queries.
 * Takes three parameters (RA, DEC and SR) that define the query, and
 * the standard returns definition parameters (see ???)
 *
 * @author mch
 */
public class SubmitCone extends StdServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      ReturnSpec tableDef = ServletHelper.makeReturnSpec(request);

      String param_ra = request.getParameter("RA");
      String param_dec = request.getParameter("DEC");
      String param_sr = request.getParameter("SR");

      if (param_ra==null) {
         doError(response, "RA missing from input parameters", null);
      }
      if (param_dec==null) {
         doError(response, "DEC missing from input parameters", null);
      }
      if (param_sr==null) {
         doError(response, "SR missing from input parameters", null);
      }
      
      try {
         double ra = Double.parseDouble(param_ra);
         double dec = Double.parseDouble(param_dec);
         double sr = Double.parseDouble(param_sr);

         //if a target is not given, we do an asynchronous (ask) Query to the response
         //stream.
         if (tableDef.getTarget() == null) {
            tableDef.setTarget(TargetIndicator.makeIndicator(response.getWriter()));
            server.askQuery(Account.ANONYMOUS, SimpleQueryMaker.makeConeQuery(ra, dec, sr, tableDef));
         }
         else {
            response.setContentType("text/html");
            response.getWriter().println(
               "<html>"+
               "<head><title>Submitting Query</title></head>"+
               "<body>");
            response.getWriter().flush();

            String id = server.submitQuery(Account.ANONYMOUS, SimpleQueryMaker.makeConeQuery(ra, dec, sr, tableDef));
      
            URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
            //indicate status
            response.getWriter().println("Cone Query has been submitted, and assigned ID "+id+"."+
                                          "<a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
            //redirect to status
            response.getWriter().write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+statusUrl+"?ID="+id+"'>"+
                                      "</body></html>");
            response.getWriter().flush();
         }
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th);
         doError(response, "Searching Cone (RA="+param_ra+", DEC="+param_dec+", SR="+param_sr+") -> "+tableDef,th);
      }
   }


}
