/*
 * $Id: InitServlet.java,v 1.9 2004/09/17 01:20:22 nw Exp $
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


import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.config.SimpleConfig;

/**
 * A simple servlet that starts cea service, by instantiating the pico container
 * on destroy, calls back into container, giving things a chance to clean themselves up if needed.
 * <p>
 * Also provides access to some of the metadata methods of the container via HTTP-GET
 * @author Noel Winstanley
 * @author Paul Harrison (pah@jb.man.ac.uk) 14-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class InitServlet extends HttpServlet {

   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(InitServlet.class);

    


 
    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (! LifecycleListener.storedEndpoint) {
            URL url = makeEndPointURL(req);
            LifecycleListener.writeEndpointConfig(url);
        }
        // expect a 'method' parameter.
        PrintWriter writer = resp.getWriter();
        String method = req.getParameter("method");
        if (method == null || method.trim().length() == 0) {
            usage(writer);
        } else if (method.trim().toLowerCase().equals("returnregistryentry")){
            resp.setContentType("text/xml");            
            returnRegistryEntry(writer);
        }
        else if (method.trim().toLowerCase().equals("getexecutionsummary")) {
            resp.setContentType("text/xml");
            String execId = req.getParameter("id");
            getExecutionSummary(writer, execId);

        }
        else if (method.trim().toLowerCase().equals("startup")) {
            logger.info("Starting CEA server");
            CEAComponentManagerFactory.getInstance();
            resp.setContentType("text/html");
            writer.println("<html><head></head><body><p>Common Execution Controller Started..</p></body></html>");
        }

        else {
            usage(writer);
        }
    }
    
    protected void returnRegistryEntry(PrintWriter pw) throws ServletException {
        try {
            String regEntry = CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry();
            pw.print(regEntry);
        } catch (CeaException e) {
            throw new ServletException(e);
        }
    }
    
    protected void getExecutionSummary(PrintWriter pw, String execId) throws ServletException {
        try {
            ExecutionSummaryType summary  = CEAComponentManagerFactory.getInstance().getQueryService().getSummary(execId);
            summary.marshal(pw);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    
   protected void usage(PrintWriter pw) {
       pw.println("<html><body>");
       pw.println("<h1>Usage</h1>");
       pw.println("<ul>");
       pw.println("<ul><tt>?method=startup</tt> - start the CommonExecutionController");
       pw.println("<li><tt>?method=returnRegistryEntry</tt> - display the VODescription for this service</li>");
       pw.println("<li><tt>?method=getExecutionSummary&amp;id=<i>executionId</i></tt> - display execution summary for <i>executionId</i>");
       pw.println("</ul>");
       pw.println("</body></html>");
   }  
   
   private URL makeEndPointURL(HttpServletRequest request) {
        URL url = null;
        try {
            url = new URL("http", request.getServerName(),
                    request.getServerPort(), request.getContextPath()
                            + "/services/CommonExecutionConnectorService");
            logger.info("creating endpointURL="+url);

        }
        catch (MalformedURLException e) {
            logger.warn("could not create the context path from request data",
                    e);
        }
        return url;

    }



}
