/*
 * $Id: DefaultServlet.java,v 1.2 2005/03/10 20:19:21 mch Exp $
 */

package org.astrogrid.webapp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.service.ServletHelper;

/**
 * A 'standard' servlet that provides standard methods for the standard
 * datacenter servlet.
 *
 * @author mch
 */
public abstract class DefaultServlet extends HttpServlet {
   
   Log log = LogFactory.getLog(DefaultServlet.class);
   
   /** Do same on POST requests as GET requests */
   public void doPost(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }
   
   /** Need to do something better than this... */
   protected void doError(HttpServletResponse response, String title, Throwable th) throws IOException {
      log.error(title, th);
//    response.sendError(response.SC_INTERNAL_SERVER_ERROR, title);
      try {
         try {
            response.setContentType("text/html");
         }
         catch (RuntimeException re) {
            //if we can't set the type, some stuff has probably already been written out. Write out the message in plain
            response.getWriter().println("------------------------------------------------------------------");
            if (title != null) {
               response.getWriter().println(ServletHelper.makeSafeForHtml(title));
            }
            if (th != null) {
               th.printStackTrace(response.getWriter());
            }
            return;
         }
         response.getWriter().print(ServletHelper.exceptionAsHtmlPage(title, th));
      }
      catch (IOException ioe) {
         log.error("Could not getWriter() on response to give error details to user");
      }
   }

}
