/*
 * $Id: SubmitAdqlSql.java,v 1.6 2004/11/09 17:42:22 mch Exp $
 */

package org.astrogrid.datacenter.servlet;
import org.astrogrid.webapp.*;

import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SqlQueryMaker;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;
import org.astrogrid.slinger.targets.TargetMaker;

/**
 * A servlet for processing Cone Queries.
 * Takes three parameters (RA, DEC and SR) that define the query, and
 * the standard returns definition parameters (see ???)
 *
 * @author mch
 */
public class SubmitAdqlSql extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      ReturnSpec tableDef = ServletHelper.makeReturnSpec(request);

      String adqlSql = request.getParameter("AdqlSql");

      try {

         if ((adqlSql == null) || (adqlSql.trim().length() == 0)) {
            throw new IllegalArgumentException("AdqlSql parameter is empty");
         }
         
      
         //if a target is not given, we do an asynchronous (ask) Query to the response
         //stream.
         if (tableDef.getTarget() == null) {
            Query query = SqlQueryMaker.makeQuery(adqlSql);
            query.getResultsDef().setTarget(TargetMaker.makeIndicator(response.getWriter(), false));
            query.getResultsDef().setFormat(tableDef.getFormat());
            server.askQuery(Account.ANONYMOUS, query);
         }
         else {
            response.setContentType("text/html");
            response.getWriter().println(
               "<html>"+
               "<head><title>Submitting Query</title></head>"+
               "<body>");

            Query query = SqlQueryMaker.makeQuery(adqlSql);
            query.getResultsDef().setTarget(tableDef.getTarget());
            query.getResultsDef().setFormat(tableDef.getFormat());
            String id = server.submitQuery(Account.ANONYMOUS, query);
      
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
