/*
 * $Id: SimpleImageAccess.java,v 1.6 2004/11/10 22:01:50 mch Exp $
 */

package org.astrogrid.datacenter.servlet;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.webapp.DefaultServlet;

/**
    * Simple image access protocol - similar to cone search but different
    * (and awkward) position format for some wierd reason.
    *
 *
 * @author mch
 */
public class SimpleImageAccess extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {

      try {
         String pos = request.getParameter("POS");
         double size = Double.parseDouble(request.getParameter("SIZE"));
         String formatList = request.getParameter("FORMAT");
   
         int comma = pos.indexOf(",");
         double ra = Double.parseDouble(pos.substring(0,comma));
         double dec = Double.parseDouble(pos.substring(comma+1));
         
         try {
            server.askQuery(ServletHelper.getUser(request), SimpleQueryMaker.makeConeQuery(ra, dec, size, new ReturnTable(TargetMaker.makeIndicator(response.getWriter(), false), formatList)));
         } catch (Throwable e) {
            doError(response, "SIAP error (RA="+ra+", DEC="+dec+", SIZE="+size+", FORMAT="+formatList+")", e);
         }
         
      } catch (NumberFormatException e) {
         doError(response, "Input parameters not correct",e);
      }
   }
}

