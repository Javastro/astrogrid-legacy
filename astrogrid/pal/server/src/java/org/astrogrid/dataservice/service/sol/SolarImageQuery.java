/*
 * $Id: SolarImageQuery.java,v 1.1 2005/03/21 18:34:46 mch Exp $
 */

package org.astrogrid.dataservice.service.sol;

import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.query.condition.CircleCondition;
import org.astrogrid.query.returns.ReturnImage;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.webapp.DefaultServlet;

/**
 * Simple solar image access. Consists of a query that returns either an image
 * or a table that
 * has URLs to the images themselves (depending on the plugin).  This class handles the initial
 * query
 *
 * @author mch
 */
public class SolarImageQuery extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {

      try {
         String dateParam = request.getParameter("datetime");
         Date date = new Date(Date.parse(dateParam));
         
         
         CircleCondition circleCon = ServletHelper.makeCircleCondition(request);
         String formatParam = request.getParameter("FORMAT");
         if (formatParam == null) formatParam = request.getParameter("format");
   
         //split formats from CSV to an array
         StringTokenizer tokenizer = new StringTokenizer(formatParam, ",");
         String[] formats = new String[tokenizer.countTokens()];
         for (int i = 0; i < formats.length; i++) {
            formats[i] = tokenizer.nextToken();
         }
         
         //now we ignore the list and only take the first one.  Rather than give
         //failbacks, teh caller should check the metadata...
         String format = formats[0];
         
         
         ReturnSpec returnSpec = null;
         boolean isTable = (ReturnTable.isTableFormat(formats));
         boolean isImage = (ReturnImage.isImageFormat(formats));
         
         //check haven't specificed both
         if ((isTable) && (isImage)) {
            throw new IllegalArgumentException("Specify either image or table format, not both.  Given="+formatParam);
         }
         if (isImage) {
            returnSpec = new ReturnImage(TargetMaker.makeTarget(response.getWriter()), format);
         }
         else {
            //default as well as if format given
            returnSpec = new ReturnTable(TargetMaker.makeTarget(response.getWriter()), format);
         }
         
         try {
            server.askQuery(LoginAccount.ANONYMOUS, new Query(circleCon, returnSpec), this);
         } catch (Throwable e) {
            doError(response, "SIAP error (RA="+circleCon.getRa()+", DEC="+circleCon.getDec()+", SIZE="+circleCon.getRadius()+", FORMAT="+formatParam+")", e);
         }
         
      } catch (NumberFormatException e) {
         doError(response, "Input parameters not correct",e);
      }
   }
}


