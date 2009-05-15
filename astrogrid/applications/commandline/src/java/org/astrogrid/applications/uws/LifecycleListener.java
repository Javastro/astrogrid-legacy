/*$Id: LifecycleListener.java,v 1.1 2009/05/15 23:11:12 pah Exp $
 * Created on 16-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.uws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.CEAComponentContainer;
///CLOVER:OFF
/** receives notifications of the servlet context (i.e. the jes webapp) starting up and shutting down. passes this on to the component manager.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Sep-2004
 * @TODO - do we still want to use this class?
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
            logger.info(" service endpoint in LifecycleListener init " + endpointURL);
            // don't do this - it forces the whole config system to startup - which is a pain if the config file isn't available yet 
            //SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.SERVICE_ENDPOINT_URL,endpointURL);
            // whack it in JNDI instead,
            if (endpointURL != null) {
               // writeEndpointConfig(endpointURL); dont do this here it is a strange jndi ref
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
 
//because of possible firewalling NAT issues, it is impossible to set this reliably - better to make this an install config issue      
//        SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.SERVICE_ENDPOINT_URL, endpointURL);
//        logger.info("service endpoint stored as "+endpointURL);
//        storedEndpoint = true;
    }
    
    static boolean storedEndpoint = false;
    
    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("Stopping component manager");
        //IMPL - there is no longer a need to stop anything?
        CEAComponentContainer.getInstance();
        logger.info("Stopped component manager");
    }

}


/* 
$Log: LifecycleListener.java,v $
Revision 1.1  2009/05/15 23:11:12  pah
moved from server project - these are mostly obsolete now and are deprecated - will be removed entirely in near future

Revision 1.7  2008/09/03 14:18:57  pah
result of merge of pah_cea_1611 branch

Revision 1.6.84.1  2008/04/08 14:45:10  pah
Completed move to using spring as container for webapp - replaced picocontainer

ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708

Revision 1.6  2005/08/10 17:45:10  clq2
cea-server-nww-improve-tests

Revision 1.5.88.1  2005/07/21 18:12:38  nw
fixed up tests - got all passing, improved coverage a little.
still could do with testing the java apps.

Revision 1.5  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.4.2.1  2004/11/15 16:56:20  pah
do not try to store the service url - firewall NAT might make this pointless - better to set in config

Revision 1.4  2004/10/08 20:00:20  pah
do not store the endpoint here - not good

Revision 1.3  2004/09/22 10:52:50  pah
getting rid of some unused imports

Revision 1.2  2004/09/17 01:20:22  nw
added lifecycle listener and threadpool

Revision 1.1.2.1  2004/09/16 18:35:33  nw
added liftcycle listener
 
*/