/*
 * $Id: InitServlet.java,v 1.3 2004/04/15 18:15:59 pah Exp $
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

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
      
      //FIXME - this is not the servlet context that we need
    
      String endpoint = conf.getServletContext().getRealPath("services/CommonExecutionConnectorService");
      logger.info("initializing service at endpoint="+endpoint);
      
      CommonExecutionConnectorClient client = DelegateFactory.createDelegate(endpoint);
      try {
         client.listApplications();
      }
      catch (CEADelegateException e) {
        throw new ServletException("problem prompting initialization of application controller", e);
      }
   }

}
