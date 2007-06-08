/*
 * $Id: SimpleSpectraQuery.java,v 1.2 2007/06/08 13:16:10 clq2 Exp $
 */

package org.astrogrid.dataservice.service.ssap;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.webapp.DefaultServlet;

/**
    * Simple image access protocol - similar to cone search but different
    * (and awkward) position format for some wierd reason.
    *
 *
 * @author M Hill
 * @author K Andrews
 * @deprecated  Building separate SSAP tool, not embedded in DSA;  also,
 * I don't think this does the right thing.
 *
 */
public class SimpleSpectraQuery extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {

      try {
        // Extract the query parameters
         double radius = ServletHelper.getRadius(request);
         double ra = ServletHelper.getRa(request);
         double dec = ServletHelper.getDec(request);
         String format = request.getParameter("FORMAT");
         if (format == null) format = request.getParameter("format");
         try {
            server.askQuery(LoginAccount.ANONYMOUS, 
                new Query(ra, dec, radius, new ReturnTable(
                        new WriterTarget(response.getWriter()), format)), 
                this);
         } catch (Throwable e) {
            doError(response, "SSAP error (RA="+Double.toString(ra)+", DEC="+Double.toString(dec)+", SIZE="+Double.toString(radius)+", FORMAT="+format+")", e);
         }
         
      } catch (NumberFormatException e) {
         doError(response, "Input parameters not correct",e);
      }
   }
}

