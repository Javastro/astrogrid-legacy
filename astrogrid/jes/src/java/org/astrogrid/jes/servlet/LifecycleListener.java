/*$Id: LifecycleListener.java,v 1.2 2004/11/05 16:52:42 jdt Exp $
 * Created on 16-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.servlet;

import org.astrogrid.jes.component.JesComponentManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/** receives notifications of the servlet context (i.e. the cea webapp) starting up and shutting down. passes this on to the component manager.
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
        // don't now if this helps - leave off for now.
       // logger.info("Starting component manager");
       // logger.info(JesComponentManagerFactory.getInstance().information()); // this does the 'start'
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("Stopping component manager");
        JesComponentManagerFactory.getInstance().stop();
        logger.info("Stopped component manager");
    }

}


/* 
$Log: LifecycleListener.java,v $
Revision 1.2  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.1.18.1  2004/11/05 15:45:26  nw
tidied imports

Revision 1.1  2004/09/16 21:44:14  nw
added lifecycle listener to shut things down cleanly
 
*/