/*
 * $Id: SimpleSpectraQuery.java,v 1.2 2005/05/27 16:21:06 clq2 Exp $
 */

package org.astrogrid.dataservice.service.ssap;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.condition.CircleCondition;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.webapp.DefaultServlet;

/**
    * Simple image access protocol - similar to cone search but different
    * (and awkward) position format for some wierd reason.
    *
 *
 * @author mch
 */
public class SimpleSpectraQuery extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {

      try {
         CircleCondition circleCon = ServletHelper.makeCircleCondition(request);
         String format = request.getParameter("FORMAT");
         if (format == null) format = request.getParameter("format");
   
         try {
            server.askQuery(LoginAccount.ANONYMOUS, new Query(circleCon, new ReturnTable(new WriterTarget(response.getWriter()), format)), this);
         } catch (Throwable e) {
            doError(response, "SSAP error (RA="+circleCon.getRa()+", DEC="+circleCon.getDec()+", SIZE="+circleCon.getRadius()+", FORMAT="+format+")", e);
         }
         
      } catch (NumberFormatException e) {
         doError(response, "Input parameters not correct",e);
      }
   }
}

