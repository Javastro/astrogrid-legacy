/*
 * $Id: ContainerListener.java,v 1.3 2004/10/25 10:43:12 jdt Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.net.MalformedURLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;

/**
 * A servlet for monitoring the Servlet container (eg tomcat)
 *
 * @author mch
 */
public class ContainerListener implements ServletContextListener  {
   
   Log log = LogFactory.getLog(ContainerListener.class);
   
   public void contextInitialized(ServletContextEvent contextEvent) {
      log.info("Datacenter "+DataServer.getDatacenterName()+" context initialising");
//      try {
         //ServletHelper.setUrlStem(contextEvent.getServletContext().getContext(".").toString());
//      }
//      catch (MalformedURLException mue) {
//         log.error("Getting servlet context resource '/'",mue);
//      }

      DataServer.startUp();
   }
   
   public void contextDestroyed(ServletContextEvent p1) {
   }
   

   
   
}
