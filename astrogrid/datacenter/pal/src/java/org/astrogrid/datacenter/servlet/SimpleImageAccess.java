/*
 * $Id: SimpleImageAccess.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;

/**
    * Simple image access protocol - similar to cone search but different
    * (and awkward) position format for some wierd reason.
    *
 *
 * @author mch
 */
public class SimpleImageAccess extends StdServlet {
   
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
            server.askQuery(Account.ANONYMOUS, new ConeQuery(ra, dec, size), new ReturnTable(new TargetIndicator(response.getWriter()), formatList));
         } catch (Throwable e) {
            doError(response, "SIAP error (RA="+ra+", DEC="+dec+", SIZE="+size+", FORMAT="+formatList+")", e);
         }
         
      } catch (NumberFormatException e) {
         doError(response, "Input parameters not correct",e);
      }
   }
}

