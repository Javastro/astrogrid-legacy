/*
 * $Id: SimpleImageQuery.java,v 1.2 2007/06/08 13:16:12 clq2 Exp $
 */

package org.astrogrid.dataservice.service.siap;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnImage;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.webapp.DefaultServlet;

/**
 * Simple image access protocol. Consists of a query that returns either an image
 * or a table that
 * has URLs to the images themselves.  This class handles the initial
 * query
 * @see http://www.ivoa.net/Documents/latest/SIA.html
 *
 * @author M Hill
 * @author K Andrews
 * @deprecated KEA: I don't believe this is the correct way to do the intial
 * query in SIAP - it is using the maths for a conesearch, but that isn't
 * correct for a SIAP image intersection I don't think...
 */
public class SimpleImageQuery extends DefaultServlet {
   
   DataServer server = new DataServer();
 
   public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {

      try {
         //CircleCondition circleCon = ServletHelper.makeCircleCondition(request);
         // Extract the query parameters
         double radius = ServletHelper.getRadius(request);
         double ra = ServletHelper.getRa(request);
         double dec = ServletHelper.getDec(request);
         
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
            returnSpec = new ReturnImage(new WriterTarget(response.getWriter()), format);
         }
         else {
            //default as well as if format given
            returnSpec = new ReturnTable(
                new WriterTarget(response.getWriter()), format);
         }
         
         try {
            //server.askQuery(LoginAccount.ANONYMOUS, new Query(circleCon, returnSpec), this);
            server.askQuery(LoginAccount.ANONYMOUS, 
                new Query(ra, dec, radius, returnSpec), 
                this
            );
         } catch (Throwable e) {
             doError(response,
                 "Pseudo-SIAP RA= " + Double.toString(ra) +
                 ", Dec = " + Double.toString(dec) +
                 ", radius = " + Double.toString(radius) +
                 ", format = " + formatParam, 
             e);

         }
         
      } catch (NumberFormatException e) {
         doError(response, "Input parameters not correct",e);
      }
   }
}


