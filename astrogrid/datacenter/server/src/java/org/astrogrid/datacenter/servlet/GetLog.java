/*
 * $Id: GetLog.java,v 1.2 2004/09/08 17:47:17 mch Exp $
 */

package org.astrogrid.datacenter.servlet;


import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.config.FailbackConfig;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.io.Piper;

/**
 * A servlet for returning the generated metadata
 *
 * @author mch
 */
public class GetLog extends StdServlet {
   
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      response.setContentType("text/plain");

      PrintWriter out = response.getWriter();
      out.println("Debug Log for "+DataServer.getDatacenterName()+" "+new Date());

      String palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/pal.log");
      String whichLog = request.getParameter("Log");
      if (whichLog != null) {
         if (request.getParameter("Log").toUpperCase().equals("CATALINA.OUT")) {
            palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/catalina.out");
         }
         else if (request.getParameter("Log").toUpperCase().equals("DEBUG")) {
            palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/catalina.out");
         }
      }
      
      out.println("From "+palDebugFilename);
      out.println("");
      out.flush();
      FileReader logReader = new FileReader(palDebugFilename);
      Piper.bufferedPipe(logReader, out);
   }


}
