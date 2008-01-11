/*
 * $Id: DsaDefaultServlet.java,v 1.2 2008/01/11 15:58:25 kea Exp $
 */

package org.astrogrid.dataservice.service.servlet;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.dataservice.service.servlet.VosiServlet;
import org.astrogrid.query.Query;
import org.astrogrid.webapp.DefaultServlet;

/**
 * A wrapper servlet to intercept incoming requests and forward them to 
 * DSA-specific handling servlets where required, or the standard default
 * servlet as a fallback.  Required because the url mapping system in 
 * web.xml does not permit enough flexibility in wildcard matching.
 *
 * @author Kona Andrews
 */
public class DsaDefaultServlet extends DefaultServlet {
   
   DataServer server = new DataServer();
   
   public void doGet(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException {

      RequestDispatcher rd = null;
      String targetUri = request.getRequestURI(); 
      if(targetUri.endsWith(VosiServlet.CAPABILITIES_SUFFIX)) {
        rd = getServletContext().getNamedDispatcher("VosiServlet");
      } 
      else if(targetUri.endsWith(VosiServlet.AVAILABILITY_SUFFIX)) {
        rd = getServletContext().getNamedDispatcher("VosiServlet");
      }
      else if(targetUri.endsWith(VosiServlet.TABLES_SUFFIX)) {
        rd = getServletContext().getNamedDispatcher("VosiServlet");
      }
      else if(targetUri.endsWith(VosiServlet.CEAAPP_SUFFIX)) {
        rd = getServletContext().getNamedDispatcher("VosiServlet");
      }
      else {
        rd = getServletContext().getNamedDispatcher("default");
      }
      if (rd == null) {
        response.sendError(response.SC_NOT_FOUND, targetUri);
      }
      else {
        rd.forward(request, response);
      }
   }
}
