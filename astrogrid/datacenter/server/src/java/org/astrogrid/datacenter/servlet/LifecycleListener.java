/*$Id: LifecycleListener.java,v 1.1 2004/09/17 01:26:55 nw Exp $
 * Created on 16-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.datacenter.queriers.StaticThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/** listens to lifecycle events, shuts down the webapp cleanly. 
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
        // do nothing.
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("shutting down cea container");
        CEAComponentManagerFactory.stop();
        logger.info("shutting down threadpool");
        StaticThreadPool.shutdown();
        logger.info("shut down");
    }

}


/* 
$Log: LifecycleListener.java,v $
Revision 1.1  2004/09/17 01:26:55  nw
added a servlet context listener - used to manage resources
 
*/