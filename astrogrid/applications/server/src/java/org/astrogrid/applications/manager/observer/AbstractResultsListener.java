/*$Id: AbstractResultsListener.java,v 1.2 2004/07/26 12:07:38 nw Exp $
 * Created on 19-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.observer;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Observable;
import java.util.Observer;

/** Abstract base class for things that want to be notified of the results of an application execution
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Jul-2004
 *
 */
public abstract class AbstractResultsListener implements Observer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AbstractResultsListener.class);

    /** Construct a new AbstractResultsListener
     * 
     */
    public AbstractResultsListener() {
        super();
    }
    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        // we only care about results..
        if (! (arg instanceof Status)) {           
            return;
        }
        Status stat = (Status) arg;
        logger.debug("Saw status " + stat.toString());
        if (stat.equals(Status.COMPLETED)) {
            logger.debug("Will notify that results are ready");            
            Application app = (Application)o;
            notifyResultsAvailable(app);   
        }
    }
    /** Subclasses to implement this.
     * Called when the application wishes to notify listeners that execution has completed and results are available
     * @param app the application that emitted the notificaiton
     * @see Application#getResult()
     */
    protected abstract void notifyResultsAvailable(Application app);   
}


/* 
$Log: AbstractResultsListener.java,v $
Revision 1.2  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.1  2004/07/20 02:03:08  nw
added abstract listener classes
 
*/