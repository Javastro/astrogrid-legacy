/*
 * $Id: SubmitCone.java,v 1.6 2005/12/07 15:55:21 clq2 Exp $
 */

package org.astrogrid.dataservice.service.cone;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.metadata.queryable.ConeConfigQueryableResource;
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.query.Query;
import org.astrogrid.query.condition.CircleCondition;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.webapp.DefaultServlet;

/**
 * A servlet for processing Cone Queries.
 * Takes three parameters (RA, DEC and SR) that define the query, and
 * the standard returns definition parameters (see ???)
 *
 * @author mch
 */
public class SubmitCone extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      CircleCondition coneCondition = ServletHelper.makeCircleCondition(request);
      ReturnSpec tableDef = ServletHelper.makeReturnSpec(request);
      SearchGroup[] coneScope = new ConeConfigQueryableResource().getSpatialGroups();
      String[] scope = new String[coneScope.length];
      for (int i = 0; i < scope.length; i++)
      {
         scope[i] = coneScope[i].getName();
      }

      Query coneQuery = new Query(scope, coneCondition, tableDef);
      
      try {

         if (tableDef.getTarget() == null) {
            //if a target is not given, we do an asynchronous (ask) Query to the response
            //stream.
            tableDef.setTarget(new WriterTarget(response.getWriter(), false));
            
            if (ServletHelper.isCountReq(request)) {
               long count = server.askCount(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
               response.setContentType(MimeTypes.PLAINTEXT);
               response.getWriter().write(""+count);
            }
            else {
               server.askQuery(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
            }
         }
         else {
            //otherwise we direct the response to the target and put status info to the browser
            response.setContentType(MimeTypes.HTML);
            response.getWriter().println(
               "<html>"+
               "<head><title>Submitting Query</title></head>"+
               "<body>"+
               "<p>Submitting, please wait...</p>");
            response.getWriter().flush();

            String id = server.submitQuery(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
      
            URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
            //indicate status
            response.getWriter().println("Cone Query has been submitted, and assigned ID "+id+"."+
                                          "<a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
            response.getWriter().flush();

            //redirect to status
            response.getWriter().write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+statusUrl+"?ID="+id+"'>"+
                                      "</body></html>");
            response.getWriter().flush();
         }
      }
      catch (java.lang.IllegalArgumentException ex) {
        //Typically thrown if a bad table name is given
        doPotentialConfigError(request, response, coneCondition, tableDef, ex);
      } 
      catch (java.lang.NullPointerException ex) {
        //Typically thrown if a bad column name is given
        doPotentialConfigError(request, response, coneCondition, tableDef, ex);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+" searching ("+coneCondition+")",th);

         doError(response, "Searching Cone ("+coneCondition+") -> "+tableDef,th);
      }
   }

   private void doPotentialConfigError(
       HttpServletRequest request, HttpServletResponse response, 
       CircleCondition coneCondition, ReturnSpec tableDef, 
       Exception ex) throws IOException {

      String errorResponseString = 
         "Searching Cone ("+coneCondition+") -> "+tableDef + "\n" +
         "<p>An error has occurred (see below); this may be because this datacenter installation is misconfigured.\n" +
         "<p>Please check that the properties 'conesearch.table', 'conesearch.ra.column', 'conesearch.dec.column' and 'conesearch.columns.units' are correctly configured in your properties file.\n";
          
      LogFactory.getLog(request.getContextPath()).error( ex+errorResponseString,ex);

      doError(response, errorResponseString, ex);
   }

}
