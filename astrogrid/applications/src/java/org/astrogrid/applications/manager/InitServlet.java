/*
 * $Id: InitServlet.java,v 1.5 2004/04/19 17:34:08 pah Exp $
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

package org.astrogrid.applications.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;

/**
 * A simple servlet that can be used to force instantion of the applicationController by calling the delegate
 * @author Paul Harrison (pah@jb.man.ac.uk) 14-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class InitServlet extends HttpServlet {

   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(InitServlet.class);
   /**
    * 
    */
   public InitServlet() {
      super();

   }

   /** 
    * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
    */
   public void init(ServletConfig conf) throws ServletException {
      // build the service endpoint & then call the service


   }

   private URL computeEndpoint(HttpServletRequest req) throws MalformedURLException {

      URL endpoint =
         new URL(
            "http",
            req.getServerName(),
            req.getServerPort(),
            req.getContextPath() + "/services/CommonExecutionConnectorService");
      return endpoint;
   }

   /** 
    * Service a get. This is used entirely to initialize the CommonExecutionController by making a call to it.
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    * @TODO Find a nicer way to case the initialization - would be better to create a container anyway...
    */
   protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
      throws ServletException, IOException {
      //TODO would be a good idea to allow regestration to specified registry...
      URL endpoint = computeEndpoint(arg0);
      logger.info("initializing service at endpoint=" + endpoint);

      CommonExecutionConnectorClient client =
         DelegateFactory.createDelegate(endpoint.toString());
      try {
         client.listApplications();
         //         arg1.setStatus(arg1.SC_OK, "?");
         // write a very simple response - not really ever viewed...
         PrintWriter out = arg1.getWriter();
         out.println("<html><body>");
         out.println("<h1>Initialising CommonExecutionController</h1>");
         out.println("</body></html>");
      }
      catch (CEADelegateException e) {
         throw new ServletException(
            "problem prompting initialization of application controller",
            e);
      }
   }

}
