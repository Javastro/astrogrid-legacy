/*
 * $Id: SubmitCone.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 */

package org.astrogrid.dataservice.service.cone;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.query.condition.CircleCondition;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.slinger.targets.TargetMaker;
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
      SearchGroup[] coneScope = VoDescriptionServer.getQueryable().getSpatialGroups();
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
            tableDef.setTarget(TargetMaker.makeTarget(response.getWriter(), false));
            server.askQuery(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
         }
         else {
            //otherwise we direct the response to the target and put status info to the browser
            response.setContentType("text/html");
            response.getWriter().println(
               "<html>"+
               "<head><title>Submitting Query</title></head>"+
               "<body>");
            response.getWriter().flush();

            String id = server.submitQuery(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
      
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
         LogFactory.getLog(request.getContextPath()).error(th+" searching ("+coneCondition+")",th);
         doError(response, "Searching Cone ("+coneCondition+") -> "+tableDef,th);
      }
   }


}
