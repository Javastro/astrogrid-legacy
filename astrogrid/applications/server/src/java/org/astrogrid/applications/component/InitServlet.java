/*
 * $Id: InitServlet.java,v 1.22 2008/10/06 12:16:16 pah Exp $
 * 
 * Created on 14-Apr-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.component.ComponentManagerException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.w3c.dom.Document;
///CLOVER:OFF
/**
 * 
 * A simple servlet that starts cea service, by instantiating the pico container
 * on destroy, calls back into container, giving things a chance to clean
 * themselves up if needed.
 * <p>
 * Also provides access to some of the metadata methods of the container via
 * HTTP-GET
 * 
 * @author Noel Winstanley
 * @author Paul Harrison (pah@jb.man.ac.uk) 14-Apr-2004
 * @version $Name:  $
 * @since iteration5
 * 
 * @deprecated the functionality of this servlet is duplicated elsewhere, but on different end points
 */
public class InitServlet extends HttpServlet {

   static private org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
         .getLog(InitServlet.class);

   /**
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
    *      javax.servlet.http.HttpServletResponse)
    */
   @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws ServletException, IOException {
      //always do it for now if (! LifecycleListener.storedEndpoint) {
      URL url = makeEndPointURL(req);
      LifecycleListener.writeEndpointConfig(url);
      //        }
      // expect a 'method' parameter.
      PrintWriter writer = resp.getWriter();
      String method = req.getParameter("method");
      if (method == null || method.trim().length() == 0) {
         usage(writer);
      } else if (method.trim().toLowerCase().equals("returnregistryentry")) {
         resp.setContentType("text/xml");
         returnRegistryEntry(writer);
      } else if (method.trim().toLowerCase().equals("getexecutionsummary")) {
         resp.setContentType("text/xml");
         String execId = req.getParameter("id");
         getExecutionSummary(writer, execId);

      } else if (method.trim().toLowerCase().equals("getexecutionlog")) {
         String execId = req.getParameter("id");
         String type = req.getParameter("type");
         getExecutionLog(resp, execId, type);

      } else if (method.trim().toLowerCase().equals("register")) {
         String endpoint = req.getParameter("endpoint");
         if (endpoint == null) {
            endpoint = (String) SimpleConfig.getSingleton().getProperty(
                  RegistryDelegateFactory.ADMIN_URL_PROPERTY);
         }
         resp.setContentType("text/html");
         try {
            writer
                  .println("<html><head></head><body><p>registered with registry at "
                        + endpoint + "</p>");
            MetadataService voProvider = CEAComponentContainer
                  .getInstance().getMetadataService();

            RegistryUploader regUploader = CEAComponentContainer
                  .getInstance().getRegistryUploaderService();
            regUploader.write(endpoint);
            writer.println("</p><p>success</p></body></html>");
         } catch (Exception e) {
            writer.println("<p>error registering</p><pre>");
            e.printStackTrace(writer);
            writer.println("</pre></body></html>");
         }

      }
      

      else if (method.trim().toLowerCase().equals("startup")) {
         logger.info("Starting CEA server");
         CEAComponentContainer.getInstance();
         resp.setContentType("text/html");
         writer
               .println("<html><head></head><body><p>Common Execution Controller Started..</p></body></html>");
      }

      else {
         usage(writer);
      }
   }

   /**
    * @param resp
    * @param execId
    * @param type
    * @throws IOException
    */
   private void getExecutionLog(HttpServletResponse resp, String execId,
         String type) throws IOException {
      ApplicationEnvironmentRetriver.StdIOType itype = ApplicationEnvironmentRetriver.StdIOType.out;
      if (type != null && type.equalsIgnoreCase("err")) {
         itype = ApplicationEnvironmentRetriver.StdIOType.err;
      }
      try {
         QueryService queryService = CEAComponentContainer.getInstance()
               .getQueryService();
         assert queryService != null : "interal error - no query service";
         File logFile = queryService.getLogFile(execId, itype);
         resp.setContentType("text/plain");
         PrintWriter pw = resp.getWriter();
         if(logFile != null)
         {
         BufferedReader inReader = new BufferedReader(new FileReader(logFile));

         String str = inReader.readLine();
         while (str != null) {
            pw.write(str);
            pw.write("\n");
            str = inReader.readLine();
         }
         }
         else
         {
            pw.write("<b>error</b> log file could not be found");
         }

      } catch (ComponentManagerException e) {
         logger.error("internal error", e);
         showError(resp, e);
      } catch (FileNotFoundException e) {
         logger.error("cannot find log file", e);
         showError(resp, e);
      } catch (CeaException e) {
         logger.error("problem getting log file", e);
         showError(resp, e);
      }
   }

   private void showError(HttpServletResponse resp, Exception e) {
      try {
         resp.setContentType("text/html");
         PrintWriter writer = resp.getWriter();
         writer.println("<html><head></head><body><p>Error.</p><p>");
         writer.println(e.getMessage());
         writer.println("</p></body></html>");
      } catch (IOException e1) {

         e1.printStackTrace();
      }

   }

   protected void returnRegistryEntry(PrintWriter pw) throws ServletException {
      Document regEntry;
    try {
	regEntry = CEAComponentContainer.getInstance()
	       .getMetadataService().returnRegistryEntry();
	    XMLUtils.DocumentToWriter(regEntry, pw);
	       } catch (Exception e) {
	throw new ServletException("problem getting registry entry", e);
    }
   }

   protected void getExecutionSummary(PrintWriter pw, String execId)
         throws ServletException {
      try {
         ExecutionSummaryType summary = CEAComponentContainer
               .getInstance().getQueryService().getSummary(execId);
        	JAXBContext jc = CEAJAXBContextFactory.newInstance();
                Marshaller m = jc.createMarshaller();
                m.marshal(summary, pw);

        

      } catch (Exception e) {
         throw new ServletException(e);
      }
   }
   
   protected void usage(PrintWriter pw) {
      pw.println("<html><body>");
      pw.println("<h1>Usage</h1>");
      pw.println("<ul>");
      pw
            .println("<ul><tt>?method=startup</tt> - start the CommonExecutionController");
      pw
            .println("<li><tt>?method=returnRegistryEntry</tt> - display the VODescription for this service</li>");
      pw
      .println("<li><tt>?method=getExecutionSummary&amp;id=<i>executionId</i></tt> - display execution summary for <i>executionId</i>");
      pw
      .println("<li><tt>?method=getexecutionlog&amp;id=<i>executionId&type=out|err</i></tt> - display execution summary for <i>executionId</i>");
      pw.println("</ul>");
      pw.println("</body></html>");
   }

   private URL makeEndPointURL(HttpServletRequest request) {
      URL url = null;
      try {
         url = new URL("http", request.getServerName(),
               request.getServerPort(), request.getContextPath()
                     + "/services/CommonExecutionConnectorService");
         logger.info("creating endpointURL=" + url);

      } catch (MalformedURLException e) {
         logger.warn("could not create the context path from request data", e);
      }
      return url;

   }

}