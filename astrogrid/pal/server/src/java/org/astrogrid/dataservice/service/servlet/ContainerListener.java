/*
 * $Id: ContainerListener.java,v 1.1.1.1 2005/02/17 18:37:35 mch Exp $
 */

package org.astrogrid.dataservice.service.servlet;

import java.net.MalformedURLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;

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
