/*
 * $Id: SimpleSpectraQuery.java,v 1.1 2004/11/11 23:23:29 mch Exp $
 */

package org.astrogrid.datacenter.ssap;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.condition.CircleCondition;
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
public class SimpleSpectraQuery extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {

      try {
         CircleCondition circleCon = ServletHelper.makeCircleCondition(request);
         String format = request.getParameter("FORMAT");
         if (format == null) format = request.getParameter("format");
   
         try {
            server.askQuery(Account.ANONYMOUS, new Query(circleCon, new ReturnTable(TargetMaker.makeIndicator(response.getWriter()), format)), this);
         } catch (Throwable e) {
            doError(response, "SSAP error (RA="+circleCon.getRa()+", DEC="+circleCon.getDec()+", SIZE="+circleCon.getRadius()+", FORMAT="+format+")", e);
         }
         
      } catch (NumberFormatException e) {
         doError(response, "Input parameters not correct",e);
      }
   }
}

