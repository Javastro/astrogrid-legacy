/*$Id: LifecycleListener.java,v 1.2 2004/09/17 01:20:22 nw Exp $
 * Created on 16-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;

import org.astrogrid.config.SimpleConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.net.URL;


import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/** receives notifications of the servlet context (i.e. the jes webapp) starting up and shutting down. passes this on to the component manager.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Sep-2004
 *
 */
public class LifecycleListener implements ServletContextListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(LifecycleListener.class);

    /** Construct a new LifecycleListener
     * 
     */
    public LifecycleListener() {
        super();
    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            URL endpointURL = arg0.getServletContext().getResource("/");//try this to see if it needs to actually exist....
            logger.info("Setting service endpoint to " + endpointURL);
            // don't do this - it forces the whole config system to startup - which is a pain if the config file isn't available yet 
            //SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.SERVICE_ENDPOINT_URL,endpointURL);
            // whack it in JNDI instead,
            if (endpointURL != null) {
                writeEndpointConfig(endpointURL);
            } else {
                logger.warn("Could not determine service endpoint");
            } 
        } catch (MalformedURLException e) {
            logger.error("Could not set service endpoint url",e);            
        }   
        // don't startup just yet.
        //logger.info("Starting component manager");
        //logger.info(CEAComponentManagerFactory.getInstance().information()); // this does the 'start'
    }
    /**
     * Writes the endpoint url to Config. The endpoint can then be picked up from
     * the config system. 
     * 
     * @param endpointURL
     * @throws NamingException
     */
    static void writeEndpointConfig(URL endpointURL) {
//        try {
//            Context root = new InitialContext();
//            String urlStr = endpointURL.toString();
//            root.rebind("java:comp/env/"
//                    + EmptyCEAComponentManager.SERVICE_ENDPOINT_URL, urlStr);
//            root.close();
//        }
//        catch (NamingException e) {
//            logger
//                    .error("Could not set service endpoint url - JNDI problem",
//                            e);
//        }
        //just use the standard config ability to write a property.
        SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.SERVICE_ENDPOINT_URL, endpointURL);
        storedEndpoint = true;
    }
    
    static boolean storedEndpoint = false;
    
    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("Stopping component manager");
        CEAComponentManagerFactory.getInstance().stop();
        logger.info("Stopped component manager");
    }

}


/* 
$Log: LifecycleListener.java,v $
Revision 1.2  2004/09/17 01:20:22  nw
added lifecycle listener and threadpool

Revision 1.1.2.1  2004/09/16 18:35:33  nw
added liftcycle listener
 
*/