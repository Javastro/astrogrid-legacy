/*$Id: AbstractProgressListener.java,v 1.1 2004/07/20 02:03:08 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;

import java.util.Observable;
import java.util.Observer;

/** Abstract base class for things that want to listen to progress of an application execution.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Jul-2004
 *
 */
public abstract class AbstractProgressListener implements Observer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AbstractProgressListener.class);

    /** Construct a new AbstractProgressListener
     * 
     */
    public AbstractProgressListener() {
        super();
    }
    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public final void update(Observable o, Object arg) {
        Application app = (Application)o;
        if (arg instanceof Status) {
            reportStatusChange(app,(Status)arg);
        } else if (arg instanceof MessageType) {
            reportMessage(app,(MessageType)arg);
            
        } else {
            logger.warn("Unknown object in update notification " + arg.getClass().getName() + " " + arg.toString());
        }
    }
    /** subclasses to implement this.
     * called when the listener encounters a message from the application.
     * @param app
     * @param type
     */
    protected abstract void reportMessage(Application app, MessageType type);    
    /** subclasses to implement this.
     * called when the listener encounters a status change to the application
     * @param app
     * @param status
     */
    protected abstract void reportStatusChange(Application app, Status status);
    
}


/* 
$Log: AbstractProgressListener.java,v $
Revision 1.1  2004/07/20 02:03:08  nw
added abstract listener classes
 
*/