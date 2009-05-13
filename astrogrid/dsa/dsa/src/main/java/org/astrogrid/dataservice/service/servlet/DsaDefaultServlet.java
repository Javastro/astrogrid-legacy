/*
 * $Id: DsaDefaultServlet.java,v 1.1 2009/05/13 13:20:34 gtr Exp $
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
      if (
        (targetUri.endsWith(VosiServlet.CAPABILITIES_SUFFIX)) ||
        (targetUri.endsWith(VosiServlet.AVAILABILITY_SUFFIX)) ||
        (targetUri.endsWith(VosiServlet.TABLES_SUFFIX)) ||
        (targetUri.endsWith(VosiServlet.CEAAPP_SUFFIX))
      ) {
        // Note: Annoyingly, if the character encoding is set in the
        // VosiServlet, it seems to have no effect (bug/feature in the 
        // servlet implementation?) - we need to set the encoding to 
        // UTF-8 in *this* servlet before handing off to VOSI.
        response.setCharacterEncoding("UTF-8");
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
