/*
 * $Id: SimpleSpectraQuery.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 */

package org.astrogrid.dataservice.service.ssap;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.query.condition.CircleCondition;
import org.astrogrid.query.returns.ReturnTable;
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
            server.askQuery(LoginAccount.ANONYMOUS, new Query(circleCon, new ReturnTable(TargetMaker.makeTarget(response.getWriter()), format)), this);
         } catch (Throwable e) {
            doError(response, "SSAP error (RA="+circleCon.getRa()+", DEC="+circleCon.getDec()+", SIZE="+circleCon.getRadius()+", FORMAT="+format+")", e);
         }
         
      } catch (NumberFormatException e) {
         doError(response, "Input parameters not correct",e);
      }
   }
}

