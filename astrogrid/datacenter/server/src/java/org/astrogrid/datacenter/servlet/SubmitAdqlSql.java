/*
 * $Id: SubmitAdqlSql.java,v 1.1 2004/09/02 08:02:17 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;
import org.astrogrid.datacenter.sqlparser.Sql2Adql074;

/**
 * A servlet for processing Cone Queries.
 * Takes three parameters (RA, DEC and SR) that define the query, and
 * the standard returns definition parameters (see ???)
 *
 * @author mch
 */
public class SubmitAdqlSql extends StdServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      ReturnSpec tableDef = ServletHelper.makeReturnSpec(request);

      String adqlSql = request.getParameter("AdqlSql");

      try {

         //if a target is not given, we do an asynchronous (ask) Query to the response
         //stream.
         if (tableDef.getTarget() == null) {
            tableDef.setTarget(new TargetIndicator(response.getWriter()));
            server.askQuery(Account.ANONYMOUS, new AdqlQuery(Sql2Adql074.translate(adqlSql)), tableDef);
         }
         else {
            response.setContentType("text/html");
            response.getWriter().println(
               "<html>"+
               "<head><title>Submitting Query</title></head>"+
               "<body>");

            String id = server.submitQuery(Account.ANONYMOUS, new AdqlQuery(adqlSql), tableDef);
      
            URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
            //indicate status
            response.getWriter().println("ADQL Query has been submitted, and assigned ID "+id+"."+
                                          "<a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
            //redirect to status
            response.getWriter().write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+statusUrl+"?ID="+id+"'>"+
                                      "</body></html>");
         }
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th);
         doError(response, "Searching ADQL: "+adqlSql+" -> "+tableDef,th);
      }
   }

}
