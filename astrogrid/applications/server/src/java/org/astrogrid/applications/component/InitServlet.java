/*
 * $Id: InitServlet.java,v 1.4 2004/07/23 13:21:21 nw Exp $
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
import org.astrogrid.config.SimpleConfig;

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
   /** if this key is set to true in config, this servlet will start up the cea server. otherwise it will start on the first request 
    * @see #init(ServletConfig)
    * */
    public static final String BOOT_CEA_KEY = "boot.cea";
    /**
     * just gets the component manager instance from the factory. 
     * as part of this, the picocontainer is started.
     * @todo doesn't seemt o want to work - url for resource is always null. arse.
     * @see #BOOT_CEA_KEY
     */
    public void init(ServletConfig arg0) throws ServletException {
        super.init(arg0);
        try {
            URL endpointURL = arg0.getServletContext().getResource("/services/CommonExecutionConnectorService");
            logger.info("Setting service endpoint to " + endpointURL);
            // don't do this - it forces the whole config system to startup - which is a pain if the config file isn't available yet 
            //SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.SERVICE_ENDPOINT_URL,endpointURL);
            // whack it in JNDI instead,
            if (endpointURL != null) {
                Context root = new InitialContext();          
                String urlStr = endpointURL.toString();
            root.rebind("java:comp/env/" + EmptyCEAComponentManager.SERVICE_ENDPOINT_URL,urlStr);   
                root.close();
            } else {
                logger.warn("Could not determine service endpoint");
            } 
        } catch (MalformedURLException e) {
            logger.error("Could not set service endpoint url",e);            
        } catch (NamingException e) {
            logger.error("Could not set service endpoint url - JNDI problem",e);
        }   
        if ("true".equalsIgnoreCase(getInitParameter(BOOT_CEA_KEY))) {   
            logger.info("Booting CEA Server");             
            CEAComponentManagerFactory.getInstance();
        }
        logger.info("InitServlet: completed init");
    }

    /**
     * @see javax.servlet.Servlet#destroy()
     */
    public void destroy() {
        CEAComponentManagerFactory.getInstance().getContainer().stop();
        CEAComponentManagerFactory.getInstance().getContainer().dispose();   
        super.destroy();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // expect a 'method' parameter.
        PrintWriter writer = resp.getWriter();
        String method = req.getParameter("method");
        if (method == null || method.trim().length() == 0) {
            usage(writer);
        } else if (method.trim().toLowerCase().equals("returnregistryentry")){
            resp.setContentType("text/xml");            
            returnRegistryEntry(writer);
        } else if (method.trim().toLowerCase().equals("getexecutionsummary")){
            resp.setContentType("text/xml");
            String execId = req.getParameter("id");
            getExecutionSummary(writer,execId);       
         } else {
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
       pw.println("<li><tt>?method=returnRegistryEntry</tt> - display the VODescription for this service</li>");
       pw.println("<li><tt>?method=getExecutionSummary&amp;id=<i>executionId</i></tt> - display execution summary for <i>executionId</i>");
       pw.println("</ul>");
       pw.println("</body></html>");
   }  

}
