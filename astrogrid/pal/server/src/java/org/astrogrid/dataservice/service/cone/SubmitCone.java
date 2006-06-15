/*
 * $Id: SubmitCone.java,v 1.7 2006/06/15 16:50:10 clq2 Exp $
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
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.webapp.DefaultServlet;

/**
 * A servlet for processing Cone Queries.
 * Takes three parameters (RA, DEC and SR) that define the query, and
 * the standard returns definition parameters (see ???)
 *
 * @author M Hill
 * @author K Andrews
 */
public class SubmitCone extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      // Extract the query parameters
      double radius = ServletHelper.getRadius(request);
      double ra = ServletHelper.getRa(request);
      double dec = ServletHelper.getDec(request);
      ReturnSpec returnSpec = ServletHelper.makeReturnSpec(request);

      try {
         // Create the query
         Query coneQuery = new Query(ra, dec, radius, returnSpec);

         if (returnSpec.getTarget() == null) {
            //if a target is not given, we do an asynchronous (ask) Query 
            // to the response stream.
            returnSpec.setTarget(new WriterTarget(response.getWriter(), false));
            
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
            //otherwise we direct the response to the target and put status 
            //info to the browser
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
        doPotentialConfigError(request, response, ra, dec, radius, ex);
      } 
      catch (java.lang.NullPointerException ex) {
        //Typically thrown if a bad column name is given
        doPotentialConfigError(request, response, ra, dec, radius, ex);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+
             "Searching Cone with RA= " + Double.toString(ra) + 
             ", Dec = " + Double.toString(dec) + 
             ", radius = " + Double.toString(radius),
             th);

         doError(response, 
             "Searching Cone with RA= " + Double.toString(ra) + 
             ", Dec = " + Double.toString(dec) + 
             ", radius = " + Double.toString(radius),
             th);
      }
   }

   private void doPotentialConfigError(
       HttpServletRequest request, HttpServletResponse response, 
       double ra, double dec, double radius,
       Exception ex) throws IOException {

      String errorResponseString = 
         "Searching Cone with RA= " + Double.toString(ra) + 
         ", Dec = " + Double.toString(dec) + ", radius = " + Double.toString(radius) +"</p>" +
         "<p>An error has occurred (see below); this may be because this datacenter installation is misconfigured.\n" +
         "<p>Please check that the properties 'conesearch.table', 'conesearch.ra.column', 'conesearch.dec.column' and 'conesearch.columns.units' are correctly configured in your properties file.\n";
          
      LogFactory.getLog(request.getContextPath()).error( ex+errorResponseString,ex);

      doError(response, errorResponseString, ex);
   }

}
