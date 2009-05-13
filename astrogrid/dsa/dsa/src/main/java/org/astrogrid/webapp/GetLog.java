/*
 * $Id: GetLog.java,v 1.1.1.1 2009/05/13 13:20:56 gtr Exp $
 */

package org.astrogrid.webapp;


import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.cfg.FailbackConfig;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.io.Piper;

/**
 * A servlet for returning logs
 *
 * @author mch
 */
public class GetLog extends DefaultServlet {
   
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      String palDebugFilename = null;
      String whichLog = request.getParameter("Log");
      if (whichLog == null) {
         palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/pal.log");
         response.setContentType("text/plain");
      }
      else if (whichLog.toUpperCase().equals("CATALINA.OUT")) {
         palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/catalina.out");
         response.setContentType("text/plain");
      }
      else if (whichLog.toUpperCase().equals("DEBUG")) {
         palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/pal-debug.log");
         response.setContentType("text/plain");
      }
      else if (whichLog.toUpperCase().equals("WEB")) {
         palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/pal-web.log");
         response.setContentType("text/html");
      }
      else if (whichLog.toUpperCase().equals("XML")) {
         palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/pal-xml.log");
         response.setContentType("text/xml");
      }
      else {
         response.setContentType("text/plain");
         response.getWriter().println("Unknown Log value '"+whichLog+"'; use CATALINA.OUT, DEBUG, XML or WEB, or leave blank for INFO level +");
      }

      if (palDebugFilename != null) {
         PrintWriter out = response.getWriter();
         out.println("Debug Log for "+DataServer.getDatacenterName()+" "+new Date());
   
         out.println("From "+palDebugFilename);
         out.println("");
         out.flush();
         FileReader logReader = new FileReader(palDebugFilename);
         Piper.bufferedPipe(logReader, out);
         out.flush();
      }
   }


}
